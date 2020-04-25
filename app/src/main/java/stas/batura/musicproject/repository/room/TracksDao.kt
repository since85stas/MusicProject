package stas.batura.musicproject.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@Dao
abstract class TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setMainPlaylistId(mainData: MainData)

    @Query ("UPDATE main_table SET current_playlist_id =:playlistId")
    abstract fun updateMainPlayilistId(playlistId: Int)

    @Query("SELECT * FROM main_table WHERE mainId= :id")
    abstract fun getMainPlaylistId(id: Long): LiveData<MainData>

    @Insert
    abstract fun insertTrack(trackKot: TrackKot)

    @Query ("SELECT * FROM tracks_table WHERE track_playlist_id = :playlistId ORDER BY id")
    abstract fun getAllTracksFromPlaylist(playlistId: Int): List<TrackKot>?

    @Query ("SELECT * FROM tracks_table ORDER BY id")
    abstract fun getAllTracks(): List<TrackKot>?

    @Query ("SELECT * FROM tracks_table WHERE track_playlist_id IN (SELECT current_playlist_id FROM main_table )")
    abstract fun getAllTracksFromMainPlaylist(): List<TrackKot>?

    @Query ("DELETE FROM tracks_table WHERE track_playlist_id = :playlistId")
    abstract fun deleteTracksInPlayList(playlistId: Int)

    @Insert
    abstract fun insertPlaylist(playlist: Playlist): Long

    @Query ("SELECT * FROM playlist_table ORDER BY playlist_id")
    abstract fun getAllPlaylists() : LiveData<List<Playlist>>

    @Query ("DELETE FROM playlist_table WHERE playlist_id = :playlistId  ")
    abstract fun deletePlaylist(playlistId: Int)


}