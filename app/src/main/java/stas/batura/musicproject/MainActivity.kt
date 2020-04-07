package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.ui.main.ControlFragment
import stas.batura.musicproject.utils.InjectorUtils

class MainActivity : AppCompatActivity() {

    private lateinit var  mainViewModel : MainAcivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ControlFragment.newInstance())
                    .commitNow()
        }

        mainViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMainViewModel(this.application))
            .get(MainAcivityViewModel::class.java)

        mainViewModel.initMusicService()

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
}
