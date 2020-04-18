package stas.batura.musicproject.repository

import androidx.lifecycle.LiveData
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao

class Repository (private val dataSource : TracksDao) : TracksDao() {

    /**
     * сохраняем информацию о треке в базе данных
     */
    override fun insertTrack(trackKot: TrackKot) {
        dataSource.insertTrack(trackKot)
    }

    /**
     * получаем список все треков
    */
    override fun getAllTracks(): LiveData<List<TrackKot>> {
        return dataSource.getAllTracks()
    }


}