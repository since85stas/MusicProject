package stas.batura.musicproject.musicservice

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import okhttp3.OkHttpClient
import stas.batura.musicproject.MainActivity
import stas.batura.musicproject.R
import java.io.File

class MusicService : Service () {

    private val NOTIFICATION_ID = 404
    private val NOTIFICATION_DEFAULT_CHANNEL_ID = "default_channel"

    // билдер для данных
    private val metadataBuilder  = MediaMetadataCompat.Builder()

    // плэйбэк
    private val stateBuilder: PlaybackStateCompat.Builder =
        PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )

    private var mediaSession: MediaSessionCompat? = null

    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null

    private var audioFocusRequested = false

    private var exoPlayer: SimpleExoPlayer? = null
    private var extractorsFactory: ExtractorsFactory? = null
    private var dataSourceFactory: DataSource.Factory? = null

    private val musicRepository: MusicRepository = MusicRepository()

    override fun onCreate() {
        super.onCreate()

        println("Music service created")

        // создаем аудио менеджер
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // создаем и настраиваем медиа сессию
        mediaSession = MediaSessionCompat(this,"Music Service")
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession!!.setCallback(mediaSessionCallback)

        val activityIntent = Intent(applicationContext, MainActivity :: class.java)

        // настраиваем активити
        mediaSession!!.setSessionActivity(
            PendingIntent.getActivity(
                applicationContext,
                0,
                activityIntent,
                0
            )
        )

        val mediaButtonIntent = Intent(
            Intent.ACTION_MEDIA_BUTTON, null, applicationContext,
            MediaButtonReceiver::class.java
        )

        // настраиваем получатель кнопок
        mediaSession!!.setMediaButtonReceiver(
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                mediaButtonIntent,
                0
            )
        )

        // создаем плеер
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )

        // добавляем слушатель
//        exoPlayer!!.addListener(exoPlayerListener)

        val httpDataSourceFactory: DataSource.Factory =
            OkHttpDataSourceFactory(
                OkHttpClient(),
                Util.getUserAgent(
                    this,
                    getString(R.string.app_name)
                )
            )

        val cache: Cache =
            SimpleCache(
                File(this.cacheDir.absolutePath + "/exoplayer"),
                LeastRecentlyUsedCacheEvictor(1024 * 1024 * 100)
            ) // 100 Mb max

        dataSourceFactory = CacheDataSourceFactory(
            cache,
            httpDataSourceFactory,
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )
        extractorsFactory = DefaultExtractorsFactory()

    }

    private val  mediaSessionCallback = object : MediaSessionCompat.Callback() {

        private var currentUri: Uri? = null
        var currentState = PlaybackStateCompat.STATE_STOPPED

        override fun onPrepare() {
            super.onPrepare()
        }

        override fun onPlay() {
            if (!exoPlayer!!.playWhenReady) {
                startService(
                    Intent(
                        applicationContext,
                        MusicService::class.java
                    )
                )
                val track: MusicRepository.Track = musicRepository.getCurrent()
                updateMetadataFromTrack(track)
                prepareToPlay(track.uri)
                if (!audioFocusRequested) {
                    audioFocusRequested = true
                    var audioFocusResult: Int
                    audioFocusResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        audioManager!!.requestAudioFocus(audioFocusRequest!!)
                    } else {
                        audioManager!!.requestAudioFocus(
                            audioFocusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN
                        )
                    }
                    if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return
                }
                mediaSession!!.isActive = true // Сразу после получения фокуса
                registerReceiver(
                    becomingNoisyReceiver,
                    IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
                )
                exoPlayer!!.playWhenReady = true
            }

            mediaSession!!.setPlaybackState(
                stateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                    1f
                ).build()
            )
            currentState = PlaybackStateCompat.STATE_PLAYING

            refreshNotificationAndForegroundStatus(currentState)
        }

        override fun onStop() {
            super.onStop()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
        }

        // подготавливаем трэк
        private fun prepareToPlay(uri: Uri) {
            if (uri != currentUri) {
                currentUri = uri
                val mediaSource =
                    ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null)
                exoPlayer!!.prepare(mediaSource)
            }
        }

        // обновляем данные о треке
        private fun updateMetadataFromTrack(track: MusicRepository.Track) {
            metadataBuilder.putBitmap(
                MediaMetadataCompat.METADATA_KEY_ART,
                BitmapFactory.decodeResource(resources, track.bitmapResId)
            )
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, track.title)
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, track.artist)
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, track.artist)
            metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, track.duration)
            mediaSession!!.setMetadata(metadataBuilder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession!!, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("Service bind")
        return PlayerServiceBinder()
    }

    // слушатель на плеер
    private val exoPlayerListener: Player.EventListener = object : Player.EventListener {
        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
        }

        override fun onLoadingChanged(isLoading: Boolean) {

        }

        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            if (playWhenReady && playbackState == ExoPlayer.STATE_ENDED) {
                mediaSessionCallback.onSkipToNext()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {

        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

        }
    }

    private val becomingNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) { // Disconnecting headphones - stop playback
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                mediaSessionCallback.onPause()
            }
        }
    }

    inner class PlayerServiceBinder : Binder () {
        fun  getMediaSessionToke() : MediaSessionCompat.Token{
            return mediaSession.sessionToken
        }
    }

    private fun refreshNotificationAndForegroundStatus(playbackState: Int) {
        when (playbackState) {
            PlaybackStateCompat.STATE_PLAYING -> {
                startForeground(NOTIFICATION_ID, getNotification(playbackState))
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                NotificationManagerCompat.from(this@PlayerService)
                    .notify(NOTIFICATION_ID, getNotification(playbackState)!!)
                stopForeground(false)
            }
            else -> {
                stopForeground(true)
            }
        }
    }

    private fun getNotification(playbackState: Int): Notification? {
        val builder =
            MediaStyleHelper.from(this, mediaSession)
        builder.addAction(
            NotificationCompat.Action(
                android.R.drawable.ic_media_previous,
                "prevoius",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
        )
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) builder.addAction(
            NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
        ) else builder.addAction(
            NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                "play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
        )
        builder.addAction(
            NotificationCompat.Action(
                android.R.drawable.ic_media_next,
                "next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
        )
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
                .setMediaSession(mediaSession!!.sessionToken)
        ) // setMediaSession требуется для Android Wear
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.color = ContextCompat.getColor(
            this,
            R.color.colorPrimaryDark
        ) // The whole background (in MediaStyle), not just icon background
        builder.setShowWhen(false)
        builder.priority = NotificationCompat.PRIORITY_HIGH
        builder.setOnlyAlertOnce(true)
        builder.setChannelId(NOTIFICATION_DEFAULT_CHANNEL_ID)
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession!!.release()
        exoPlayer!!.release()
    }
}