package stas.batura.musicproject

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.musicproject.musicservice.MusicService

class MainActivity : AppCompatActivity() {

    private var playerServiceBinder: MusicService.PlayerServiceBinder? = null
    private var mediaController: MediaControllerCompat? = null
    private var callback: MediaControllerCompat.Callback? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }
        initMusicService()
    }

    private fun initMusicService() {

        // реагируем на изменение колбека
        callback = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                if (state == null) return
                val playing = state.state == PlaybackStateCompat.STATE_PLAYING
                play_button.setEnabled(!playing)
                pause_button.setEnabled(playing)
                stop_button.setEnabled(playing)
            }
        }

        // соединение с сервисом
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                playerServiceBinder = service as MusicService.PlayerServiceBinder
                try {
                    mediaController = MediaControllerCompat(
                        this@MainActivity,
                        playerServiceBinder!!.getMediaSessionToke()
                    )
                    mediaController!!.registerCallback(callback!!)
                    callback!!.onPlaybackStateChanged(mediaController!!.playbackState)
                } catch (e: RemoteException) {
                    mediaController = null
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                playerServiceBinder = null
                if (mediaController != null) {
                    mediaController!!.unregisterCallback(callback!!)
                    mediaController = null
                }
            }
        }

        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection!!,
            Context.BIND_AUTO_CREATE
        )

        play_button.setOnClickListener(View.OnClickListener { if (mediaController != null) mediaController!!.transportControls.play() })

        pause_button.setOnClickListener(View.OnClickListener { if (mediaController != null) mediaController!!.transportControls.pause() })

        stop_button.setOnClickListener(View.OnClickListener { if (mediaController != null) mediaController!!.transportControls.stop() })

        skip_to_next_button.setOnClickListener(View.OnClickListener { if (mediaController != null) mediaController!!.transportControls.skipToNext() })

        skip_to_previous_button.setOnClickListener(View.OnClickListener { if (mediaController != null) mediaController!!.transportControls.skipToPrevious() })

    }
}
