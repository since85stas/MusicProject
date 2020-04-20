package stas.batura.musicproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
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
     * получаем список все треков
    */
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
     * удаляем все трэки из плэйлиста
     */
    override fun deleteTracksInPlayList() {
        ioScope.launch {
            dataSource.deleteTracksInPlayList()
        }
    }
}