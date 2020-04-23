package stas.batura.musicproject

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.net.Uri
import android.os.Environment
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
import java.io.File

class MainAcivityViewModel (private val application: Application,
                            private val database:TracksDao) : ViewModel(  ) {

    // music repository
    val musicRepository : MusicRepository = MusicRepository.getInstance(application)

    // database repository
    private val repository : Repository = Repository(database)

    //
    private val trackDbLive = repository.getAllTracks()

    private var playerServiceBinder: MusicService.PlayerServiceBinder? = null
    var mediaController: MutableLiveData<MediaControllerCompat?> = MutableLiveData()
    private var callback: MediaControllerCompat.Callback? = null
    var serviceConnection: MutableLiveData<ServiceConnection?> = MutableLiveData()

//    private var  LiveData<>

    val callbackChanges : MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)

    private var _createServiceListner : MutableLiveData<Boolean> = MutableLiveData(false)
    val createServiceListner : LiveData<Boolean>
        get() = _createServiceListner

    init {
        println("init main view model")
//        print("Repo is $musicRepository")
//        initMusicService()
        val trackKot = TrackKot(0,"Triangle",
                    "Jason Shaw",
                    "album",
                    R.drawable.image266680,
                    Uri.fromFile(
                        File(
                            Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/Music/Moonspell/Studio and Compilation/1995-Wolfheart (Original 1CD Release)/02 Love Crimes.mp3")
                    ),
                    (3 * 60 + 41) * 1000)

//        repository.insertTrack(trackKot)

        // создаем сервис
//        checkServiseCreation()
    }


    /**
     * запускает создание сервиса
     */
    fun checkServiseCreation() {
        _createServiceListner.value = true
        _createServiceListner.value = false
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
                        mediaController!!.value!!.registerCallback(callback!!)
                        callback!!.onPlaybackStateChanged(mediaController!!.value!!.playbackState)
                    } catch (e: RemoteException) {
                        mediaController.value = null
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    playerServiceBinder = null
                    if (mediaController != null) {
                        mediaController!!.value!!.unregisterCallback(callback!!)
                        mediaController.value = null
                    }
                }
            }
        }
    }


    fun playClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.play()
    }

    fun pauseyClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.pause()
    }

    fun stopClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.stop()
    }

    fun nextClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.skipToNext()
    }

    fun prevClicked () {
        if (mediaController.value != null) mediaController.value!!.transportControls.skipToPrevious()
    }

    fun onItemClicked (uri: Uri) {
        if (mediaController.value != null) mediaController.value!!.transportControls.playFromUri(uri, null)
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
