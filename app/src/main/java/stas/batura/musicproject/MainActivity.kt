package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.ui.control.ControlFragment
import stas.batura.musicproject.utils.InjectorUtils

class MainActivity : AppCompatActivity(), View.OnTouchListener {

    lateinit var  mainViewModel : MainAcivityViewModel

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // создаем начальный фрагмент
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ControlFragment.newInstance())
                    .commitNow()
        }

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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {


        return false
    }
}
