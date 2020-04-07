package stas.batura.musicproject

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stas.batura.musicproject.ui.main.MainAcivityViewModel

class MainActivityViewModelFactory(
        private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainAcivityViewModel::class.java)) {
            return MainAcivityViewModel( application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}