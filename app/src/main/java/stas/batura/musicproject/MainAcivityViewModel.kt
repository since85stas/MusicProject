package stas.batura.musicproject

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.repository.Repository
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao

class MainAcivityViewModel (private val application: Application,
                            private val database:TracksDao) : ViewModel(  ) {

    // music repository
    private val musicRepository : MusicRepository = MusicRepository.getInstance()

    // database repository
    private val repository : Repository = Repository(database)

    //
    private val trackDbLive = repository.getAllTracks()

    private var playerServiceBinder: MusicService.PlayerServiceBinder? = null
    private var mediaController: MediaControllerCompat? = null
    private var callback: MediaControllerCompat.Callback? = null
    var serviceConnection: ServiceConnection? = null

    val callbackChanges : MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)

    private var _createServiceListner : MutableLiveData<Boolean> = MutableLiveData()
    val createServiceListner : LiveData<Boolean>
        get() = _createServiceListner

    init {
        println("init main view model")
        print("Repo is $musicRepository")
//        initMusicService()
    }

    fun checkServiseCreation() {
        _createServiceListner.value = true
        _createServiceListner.value = false
    }

    /**
     * создает музыкальный сервис и его контроллер
     */
    fun initMusicService(isRecreate : Boolean) {
        if (serviceConnection == null || isRecreate) {
            // привязываем колбека и лайв дэйта
            callback = object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    callbackChanges.value = state
                }
            }

            // соединение с сервисом
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    playerServiceBinder = service as MusicService.PlayerServiceBinder
                    try {
                        mediaController = MediaControllerCompat(
                            application,
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
        }
    }


    fun playClicked () {
        if (mediaController != null) mediaController!!.transportControls.play()
    }

    fun pauseyClicked () {
        if (mediaController != null) mediaController!!.transportControls.pause()
    }

    fun stopClicked () {
        if (mediaController != null) mediaController!!.transportControls.stop()
    }

    fun nextClicked () {
        if (mediaController != null) mediaController!!.transportControls.skipToNext()
    }

    fun prevClicked () {
        if (mediaController != null) mediaController!!.transportControls.skipToPrevious()
    }

    fun onItemClicked (uri: Uri) {
        if (mediaController != null) mediaController!!.transportControls.playFromUri(uri, null)
    }

    /**
     * добавляем трек в базу данных
     */
    fun addTrackToDb(trackKot: TrackKot) {
        repository.insertTrack(trackKot)
    }

    /**
     * фабрика для создания модели
     */
    class Factory(
        private val application: Application,
        private val data : TracksDao
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainAcivityViewModel::class.java)) {
//                mainAcivityViewModel =
                return MainAcivityViewModel(application, data) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



}
