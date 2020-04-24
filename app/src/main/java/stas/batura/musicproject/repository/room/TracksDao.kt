package stas.batura.musicproject.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@Dao
abstract class TracksDao {

    @Insert
    abstract fun insertTrack(trackKot: TrackKot)

    @Query ("SELECT * FROM tracks_table ORDER BY id")
    abstract fun getAllTracks() : List<TrackKot>?

    @Query ("DELETE FROM tracks_table")
    abstract fun deleteTracksInPlayList()

    @Insert
    abstract fun insertPlaylist(playlist: Playlist)


}