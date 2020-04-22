package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.repository.room.TracksDatabase
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager
import java.io.File


class MainActivity : AppCompatActivity() {

    private var x1   = 0f
    private  var x2  = 0f
    val MIN_DISTANCE = 150

    val TAG = MainActivity::javaClass.toString()

    lateinit var  mainViewModel : MainAcivityViewModel

    private lateinit var navContr : NavController

//    private lateinit var dataSource : TracksDao

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        dataSource = TracksDatabase.getInstance(this).tracksDatabaseDao

        navContr = Navigation.findNavController(this, R.id.nav_host_fragment)

        mainViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMainViewModel(this.application))
            .get(MainAcivityViewModel::class.java)

        mainViewModel.createServiceListner.observe(this, Observer {
            if (it) {
                createMusicService()
            }
        })

        mainViewModel.serviceConnection.observe(this, Observer {
            if (it != null) {
                print("service created")
//                mainViewModel.playClicked()
            }
        })

        mainViewModel.mediaController.observe(this, Observer {
            if (it != null) {
                mainViewModel.playClicked()
            }
        })

        val songsManager = SongsManager();
        val playlist = songsManager.playList

//        createMusicService()
    }


    /**
     * создаем и привязываем сервис
     */
    private fun createMusicService() {
        // инициализируем муз сервис
        mainViewModel.initMusicService(false)

        // привязываем сервис к активити
        bindService(Intent(applicationContext!!, MusicService::class.java),
            mainViewModel.serviceConnection!!.value!!,
            Context.BIND_AUTO_CREATE)
    }


    override fun onPause() {
        println("main activity pause")
        super.onPause()
    }

    override fun onStop() {
        println("main activity stop")
        super.onStop()
    }

    /**
     * навигация в фрагмент с плейлистом
     */
    private fun navigateToLeft () {
        navContr.navigate(R.id.playlistFragment)
    }

    /**
     * навигация с фрагментом с управлением
     */
    private fun navigateToRight () {
        navContr.navigate(R.id.controlFragment)
    }

    /**
     * проверяем слайд вправо и влево
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val myAction: Int = MotionEventCompat.getActionMasked(event)

        return when (myAction) {
            MotionEvent.ACTION_UP -> {
                x2 = event!!.x
                val deltaX = x2 - x1
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                        navigateToRight()
                    } else {
                        navigateToLeft()
                    }
                }
                true
            }

            MotionEvent.ACTION_DOWN -> {
                x1 = event!!.getX()
                true
            }

            MotionEvent.ACTION_MOVE -> {
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                true
            }
            else -> super.onTouchEvent(event)
        }
    }


}
