package stas.batura.musicproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import stas.batura.musicproject.repository.room.MainData
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao

class Repository (private val dataSource : TracksDao) : TracksDao() {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var repositoryJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [repositoryJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val ioScope = CoroutineScope(Dispatchers.IO + repositoryJob)

    //----------------------------MAIN PART---------------------------------------------------------
    override fun setMainPlaylistId(mainData: MainData) {
        ioScope.launch {
            dataSource.setMainPlaylistId(mainData)
        }
    }

    override fun updateMainPlayilistId(playlistId: Int) {
        ioScope.launch {
            dataSource.updateMainPlayilistId(playlistId)
        }
    }

    fun getNewMainPlaylistId(): LiveData<MainData> {
        val id = 0L
        return getMainPlaylistId(id)
    }

    override fun getMainPlaylistId(id: Long): LiveData<MainData> {
        return dataSource.getMainPlaylistId(id)
    }

//----------------------------TRAKS PART---------------------------------------------------------
    /**
     * сохраняем информацию о треке в базе данных
     */
    override fun insertTrack(trackKot: TrackKot) {
        ioScope.launch {
            dataSource.insertTrack(trackKot)
        }
    }

    /**
     * сохраняем список треков
     */
    fun insertTracks(tracks: List<TrackKot>) {
            for (track:TrackKot in tracks) {
                insertTrack(track)
            }
    }

    /**
     * получаем список все треков в заданном плейлисте
    */
    override fun getAllTracksFromMainPlaylist(): List<TrackKot>? {
        var result : List<TrackKot>? = null
            runBlocking {
                ioScope.async {
                    result = dataSource.getAllTracksFromMainPlaylist()
                }.await()
            }
        return result
    }

    /**
     * получаем список все треков в вфбранном плейлисте
     */
    override fun getAllTracksFromPlaylist(playlistId: Int): List<TrackKot>? {
        var result : List<TrackKot>? = null
        runBlocking {
            ioScope.async {
                result = dataSource.getAllTracksFromPlaylist(playlistId)
            }.await()
        }
        return result
    }

    override fun getAllTracks(): List<TrackKot>? {
        var result : List<TrackKot>? = null
        runBlocking {
            ioScope.async {
                result = dataSource.getAllTracks()
            }.await()
        }
        return result
    }

    /**
     * удаляем все трэки из основного плэйлиста
     */
    override fun deleteTracksInMainPlayList() {
        ioScope.launch {
            dataSource.deleteTracksInMainPlayList()
        }
    }

    /**
     * удаляем все трэки из плэйлиста
     */
    override fun deleteTracksInPlayList(playlistId: Int) {
        ioScope.launch {
            dataSource.deleteTracksInPlayList(playlistId)
        }
    }

    /**
     * отмечаем играющий трек
     */
    override fun setTrackIsPlaying(trackId: Int) {
        runBlocking {
            ioScope.async {
                dataSource.setTrackIsPlaying(trackId)
            }.await()
        }
    }

    //----------------------------PLAYLIST PART---------------------------------------------------------
    /**
     * вставляем новый плейлист в базу
     */
    override fun insertPlaylist(playlist: Playlist): Long {
        var result: Long = 0
        runBlocking {
            ioScope.async {
                result = dataSource.insertPlaylist(playlist)
            }.await()
        }
        return result
    }

    /**
     * получаем список всех плейлистов
     */
    override fun getAllPlaylists(): LiveData<List<Playlist>> {
        return dataSource.getAllPlaylists()
    }


}