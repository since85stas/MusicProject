package stas.batura.musicproject.ui.playlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.musicservice.MusicRepository

class PlaylistViewModel ( val application: Application) : ViewModel () {

    private var _songListViewModel : MutableLiveData<List<MusicRepository.Track>?> =
        MusicRepository.getInstance(application).tracks

    val songListViewModel : LiveData<List<MusicRepository.Track>?>
    get() = _songListViewModel

    val musicRepository : MusicRepository = MusicRepository.getInstance(application)

    init {
        print("playlist init")
//        _songListViewModel = musicRepository.tracks
    }

    /**
     * фабрика для создания модели
     */
    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
//                mainAcivityViewModel =
                return PlaylistViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


    fun addTrack() {
        musicRepository.addTrack()
    }
}