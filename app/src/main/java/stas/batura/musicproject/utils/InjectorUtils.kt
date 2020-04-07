package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import stas.batura.musicproject.MainAcivityViewModel

object InjectorUtils {

    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {

        return MainAcivityViewModel.Factory(application)
    }

}