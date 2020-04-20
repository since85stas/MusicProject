package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.repository.room.TracksDatabase
import stas.batura.musicproject.ui.playlist.PlaylistViewModel

object InjectorUtils {

    var tracksDao : TracksDao? = null

    private fun provideDao (application: Application) : TracksDao {
        if (tracksDao == null) {
            tracksDao = TracksDatabase.getInstance(application).tracksDatabaseDao
        }
        return tracksDao!!
    }

    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {
        val dataSource = TracksDatabase.getInstance(application).tracksDatabaseDao
        return MainAcivityViewModel.Factory(application, dataSource)
    }

    fun providePlaylistViewModel (application: Application) : PlaylistViewModel.Factory {
        return PlaylistViewModel.Factory(application, provideDao(application))
    }

}