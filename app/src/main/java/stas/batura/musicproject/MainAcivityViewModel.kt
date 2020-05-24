package stas.batura.musicproject

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.repository.room.MainData
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager
import java.lang.Exception
import java.lang.NullPointerException

enum class NetApiStatus { LOADING, ERROR, DONE }

class MainAcivityViewModel (private val application: Application,
                            val repository: TracksDao,
                            val musicRepository: MusicRepository
                            ) : ViewModel(  ) {

    private var playerServiceBinder: MusicService.PlayerServiceBinder? = null
    var mediaController: MutableLiveData<MediaControllerCompat?> = MutableLiveData()
    private var callback: MediaControllerCompat.Callback? = null
    var serviceConnection: MutableLiveData<ServiceConnection?> = MutableLiveData()
    var exoPlayer: MutableLiveData<ExoPlayer> = MutableLiveData()

    val callbackChanges : MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)

    private var _createServiceListner : MutableLiveData<Boolean> = MutableLiveData(false)
    val createServiceListner : LiveData<Boolean>
        get() = _createServiceListner

    private val _netStatus: MutableLiveData<NetApiStatus> = MutableLiveData()
    val netStatus: LiveData<NetApiStatus>
        get() = _netStatus

    var songText: MutableLiveData<String> = MutableLiveData()

    private var _openTextListner : MutableLiveData<Boolean> = MutableLiveData(false)
    val openTextListner : LiveData<Boolean>
        get() = _openTextListner

    val mainDataLive = repository.getMainPlaylistId(0L)

    val playlistListLive:LiveData<List<Playlist>> = repository.getAllPlaylists()

    val currentTrackPlaying = repository.getPlayingTrack()

    val controlsLive = repository.getControls()

    val currentPlaylistName = repository.getCurrPlaylistName()

    var playIsClicked: Boolean = false

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        println("init main view model")
    }

    /**
     * запускает создание сервиса
     */
    fun checkServiseCreation() {
        if ( musicRepository.tracks.value == null || musicRepository.tracks.value!!.size == 0) {
            Log.d("mainView", "empty music rep")
        } else if (!_createServiceListner.value!! ) {
            playIsClicked = true
            _createServiceListner.value = true
        } else {
//            playClicked()
            changePlayState()
        }
//        _createServiceListner.value = false
    }

    /**
     * создает музыкальный сервис и его контроллер
     */
    fun initMusicService(isRecreate : Boolean) {
        if (serviceConnection.value == null || isRecreate) {
            // привязываем колбека и лайв дэйта
            callback = object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    callbackChanges.value = state
                }
            }


            // соединение с сервисом
            serviceConnection.value = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    playerServiceBinder = service as MusicService.PlayerServiceBinder
                    try {
                        mediaController.value = MediaControllerCompat(
                            application,
                            playerServiceBinder!!.getMediaSessionToke()
                        )

                        exoPlayer.value = playerServiceBinder!!.getPlayer()

                        mediaController.value!!.registerCallback(callback!!)
                        callback!!.onPlaybackStateChanged(mediaController.value!!.playbackState)
                    } catch (e: RemoteException) {
                        mediaController.value = null
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    playerServiceBinder = null
                    if (mediaController.value != null) {
                        mediaController.value!!.unregisterCallback(callback!!)
                        mediaController.value = null
                    }
                    _createServiceListner.value = false
                }
            }
        }
    }

    fun changePlayState() {
        if (mediaController.value != null) {
            if (callbackChanges.value!!.state == PlaybackStateCompat.STATE_PLAYING) {
                mediaController.value!!.transportControls.pause()
            } else {
                mediaController.value!!.transportControls.play()
            }
        }
    }

    fun playClicked () {
        if (mediaController.value != null) {

            mediaController.value!!.transportControls.play()
        }
    }

    fun pauseyClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.pause()
    }

    fun stopClicked () {
        if (mediaController.value != null) {
            mediaController.value!!.transportControls.stop()
            repository.setAllTrackIsNOTPlaying()
            musicRepository.getDbTracks()
        }
    }

    fun nextClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.skipToNext()
    }

    fun prevClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.skipToPrevious()
    }

    fun onItemClicked (uri: Uri) {
//        initMusicService(false)
        musicRepository.setPlayByUri(uri)
        checkServiseCreation()
//        if (mediaController.value != null) mediaController.value!!.transportControls.playFromUri(uri, null)
    }

    fun onItemClickedPlayed(uri: Uri) {
        musicRepository.setPlayByUri(uri)
//        playClicked()
        mediaController.value!!.transportControls.playFromUri(uri, null)
    }

    /**
     * добавляем трек в базу данных
     */
    fun addTrackToDb(trackKot: TrackKot) {
        repository.insertTrack(trackKot)
    }

    /**
     * добавляем новый плейлист в базу
     */
    fun addNewPlaylist(name:String) {
        val newPlaylistId = repository.insertPlaylist(Playlist(name))
        repository.setMainPlaylistId(MainData(newPlaylistId.toInt()))
        musicRepository.getDbTracks()
    }

    /**
     * при нажатии на выбор плейлиста
     */
    fun onNavPlaylistItemClicked(playlistId: Int) {
        repository.setMainPlaylistId(MainData(playlistId))
        musicRepository.getDbTracks()
    }


    fun deleteTrackFromPl(id: Int) {
        repository.deleteTrack(id)
        musicRepository.getDbTracks()
    }

    fun onActivityDestroyed() {
        setAllTracksNotPlaying()
        _createServiceListner.value = false
    }

    fun onActivityCreated() {
        repository.setAllTrackIsNOTPlaying()
//        checkServiseCreation()
    }

    fun setAllTracksNotPlaying() {
        repository.setAllTrackIsNOTPlaying()
    }

    fun setRepeatStatus(staus: Int) {
        repository.changerPlayStatus(staus)
    }

    fun getTrackText() {
        if (currentTrackPlaying.value != null) {
            coroutineScope.launch {
                val resultDeffered = InjectorUtils.provideRetrofit().getSongText(
                    currentTrackPlaying.value!!.title,
                    currentTrackPlaying.value!!.artist
                )
                try {
                    _netStatus.value = NetApiStatus.LOADING
                    val bytes = resultDeffered.await().lyrics
                    songText.value = bytes
                    _netStatus.value = NetApiStatus.DONE

                } catch (e: Exception) {
                    Log.d("eee", e.toString())
                    _netStatus.value = NetApiStatus.ERROR
                } finally {
                    Log.d("eee", "finally")
//                _buttonCliked.value = false
                }
            }
            _openTextListner.value = true
        }

    }

    fun openTextDialogFinish() {
        _openTextListner.value = false
    }

    /**
     * удаляет плейлист
     */
    fun deletePlaylist() {
        repository.deleteTracksInMainPlayList()
        repository.deletePlaylist()

        repository.updateMainPlayilistId(0)
        musicRepository.getDbTracks()
    }

    /**
     * поллучаем путь папки, создаем список треков и сохраняем в БД
     */
    fun addTracksToPlaylist(pathStr : String) {
        val songsManager = SongsManager(pathStr, mainDataLive.value!!.currentPlaylistId);
        val songs = songsManager.playList
        repository.insertTracks(songs)
        musicRepository.getDbTracks()

        updatePlaylistName(songsManager.playlistName)
//        musicRepository = MusicRepository.recreateMusicRepository(application)
    }

    /**
     * обновляет имя текущего плейлиста в БД
     */
    fun updatePlaylistName(name: String) {
        repository.updatePlaylistName(mainDataLive.value!!.currentPlaylistId, name)
    }


    /**
     * фабрика для создания модели
     */
    class Factory(
        private val application: Application,
        private val repository: TracksDao,
        private val musicRepository: MusicRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainAcivityViewModel::class.java)) {
                return MainAcivityViewModel(application, repository, musicRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



}
