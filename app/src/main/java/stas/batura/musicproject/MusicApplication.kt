package stas.batura.musicproject

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelStore

class MusicApplication() :Application() {

    lateinit var modelStore: ViewModelStore

    override fun onCreate() {
        modelStore = ViewModelStore()
        super.onCreate()
        Log.d("app", "created")
    }


}