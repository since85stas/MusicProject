package stas.batura.musicproject.musicservice

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MusicService : Service () {

    override fun onCreate() {

        println("Music service created")

        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("Service bind")
        return null
    }
}