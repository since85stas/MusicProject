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
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.ui.main.MainAcivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var  viewModel : MainAcivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }
        val factory = MainActivityViewModelFactory(application)
        viewModel = ViewModelProviders.of(this,factory)
            .get(MainAcivityViewModel::class.java)

        play_button.setOnClickListener { viewModel.playClicked() }
        pause_button.setOnClickListener {viewModel.pauseyClicked()}
        stop_button.setOnClickListener {viewModel.stopClicked()}

        viewModel.initMusicService()

        bindService(Intent(applicationContext!!, MusicService::class.java),
            viewModel.serviceConnection!!,
            Context.BIND_AUTO_CREATE)

    }

//    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
//        return super.bindService(Intent(applicationContext!!, MusicService::class.java),
//            viewModel.serviceConnection!!,
//            Context.BIND_AUTO_CREATE)
//    }

//    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
//        return super.bindService(service, conn, flags)
//    }

//    bindService(
//    Intent(application, MusicService::class.java),
//    serviceConnection!!,
//    Context.BIND_AUTO_CREATE
//    )



}
