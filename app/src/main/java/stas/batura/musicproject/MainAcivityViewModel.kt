package stas.batura.musicproject

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.musicproject.musicservice.MusicService

class MainAcivityViewModel (val application: Application) : ViewModel(  ) {

    private var playerServiceBinder: MusicService.PlayerServiceBinder? = null
    private var mediaController: MediaControllerCompat? = null
    private var callback: MediaControllerCompat.Callback? = null
    var serviceConnection: ServiceConnection? = null

    val callbackChanges : MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)

    init {
        println("init main view model")
//        initMusicService()
    }


    fun initMusicService() {

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

    /**
     * фабрика для создания модели
     */
    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainAcivityViewModel::class.java)) {
//                mainAcivityViewModel =
                return MainAcivityViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
