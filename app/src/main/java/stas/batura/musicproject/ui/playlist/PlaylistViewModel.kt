package stas.batura.musicproject.ui.playlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.Repository
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.utils.SongsManager

class PlaylistViewModel ( val repository: TracksDao,
                          val musicRepository: MusicRepository) : ViewModel () {

//    val musicRepository : MusicRepository = MusicRepository.getInstance(application)

    // смотрим за списком трэков для отображения в плейлисте
    private var _songListViewModel : MutableLiveData<List<MusicRepository.Track>?> =
        musicRepository.tracks
    val songListViewModel : LiveData<List<MusicRepository.Track>?>
    get() = _songListViewModel

    // смотрим за нажатием кнопуи добавить
    private var _addButtonClicked : MutableLiveData<Boolean> = MutableLiveData(false)
    val addButtonClicked : LiveData<Boolean>
    get () = _addButtonClicked

    // смотрим за нажатием кнопуи выбора плейлиста
    private var _playlistNameClicked : MutableLiveData<Boolean> = MutableLiveData(false)
    val playlistNameClicked : LiveData<Boolean>
        get () = _playlistNameClicked

    val playlistName = repository.getCurrPlaylistName()

//    val repository: Repository = Repository(tracksDao)

    val mainDataLive = repository.getMainPlaylistId(0L)

    init {
        print("playlist init")
//        _songListViewModel = musicRepository.tracks
    }

    /**
     * вызывается при нажатии на кнопку добавления
     */
    fun addButtonClicked() {
        _addButtonClicked.value = true
    }

    /**
     * оканчание нажатия на кнопку
     */
    fun addButtonClickedFinish() {
        _addButtonClicked.value = false
    }

    fun playlistNameClickedFun() {
        _playlistNameClicked.value = true
        _playlistNameClicked.value = false
    }

    /**
     * удаляем все треки из выбранного плэйлиста
     */
    fun deleteButtonclicked() {
        repository.deleteTracksInMainPlayList()
//        musicRepository = MusicRepository.recreateMusicRepository(application)
        musicRepository.getDbTracks()
    }

    /**
     * поллучаем путь папки, создаем список треков и сохраняем в БД
     */
    fun addTracksToPlaylist(pathStr : String) {
        val songsManager = SongsManager(pathStr, mainDataLive.value!!.currentPlaylistId);
        val songs = songsManager.playList
        repository.insertTracks(songs)
        musicRepository.getDbTracks()

        updatePlaylistName(songsManager.playlistName)
//        musicRepository = MusicRepository.recreateMusicRepository(application)
    }

    fun updatePlaylistName(name: String) {
        repository.updatePlaylistName(mainDataLive.value!!.currentPlaylistId, name)
    }

    /**
     * фабрика для создания модели
     */
    class Factory(
        private val repository: TracksDao,
        private val musicRepository: MusicRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
//                mainAcivityViewModel =
                return PlaylistViewModel(repository, musicRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}