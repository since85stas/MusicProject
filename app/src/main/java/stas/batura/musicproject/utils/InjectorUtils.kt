package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.ui.playlist.PlaylistViewModel

object InjectorUtils {


    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {
        return MainAcivityViewModel.Factory(application)
    }

    fun providePlaylistViewModel (application: Application) : PlaylistViewModel.Factory {
        return PlaylistViewModel.Factory(application)
    }

}