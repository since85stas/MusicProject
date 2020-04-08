package stas.batura.musicproject.ui.playlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.musicservice.MusicRepository

class PlaylistViewModel ( val application: Application) : ViewModel () {

    private var _songListViewModel : MutableLiveData<Array<MusicRepository.Track>?> = MutableLiveData(null);
    val songListViewModel : LiveData<Array<MusicRepository.Track>?>
    get() = _songListViewModel

    val musicRepository : MusicRepository = MusicRepository()

    init {
        print("playlist init")

        _songListViewModel.value = musicRepository.data
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
}