package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.repository.room.TracksDatabase
import stas.batura.musicproject.ui.playlist.PlaylistViewModel

object InjectorUtils {

    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {
        val dataSource = TracksDatabase.getInstance(application).tracksDatabaseDao
        return MainAcivityViewModel.Factory(application, dataSource)
    }

    fun providePlaylistViewModel (application: Application) : PlaylistViewModel.Factory {
        return PlaylistViewModel.Factory(application)
    }

}