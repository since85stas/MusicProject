package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.utils.InjectorUtils


class MainActivity : AppCompatActivity() {

    private var x1   = 0f
    private  var x2  = 0f
    val MIN_DISTANCE = 150

    val TAG = MainActivity::javaClass.toString()

    lateinit var  mainViewModel : MainAcivityViewModel

    private lateinit var navContr : NavController

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navContr = Navigation.findNavController(this, R.id.nav_host_fragment)

        // создаем начальный фрагмент
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, ControlFragment.newInstance())
//                    .commitNow()
//        }

        mainViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMainViewModel(this.application))
            .get(MainAcivityViewModel::class.java)

        // инициализируем муз сервис
        mainViewModel.initMusicService()

        // привязываем сервис к активити
        bindService(Intent(applicationContext!!, MusicService::class.java),
            mainViewModel.serviceConnection!!,
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

    private fun navigateToLeft () {
        navContr.navigate(R.id.playlistFragment)
    }

    private fun navigateToRight () {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val myAction: Int = MotionEventCompat.getActionMasked(event)

        return when (myAction) {
            MotionEvent.ACTION_UP -> {
                x2 = event!!.x
                val deltaX = x2 - x1
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                        println("right")
                    } else {
                        navigateToLeft()
                        println("left")
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
