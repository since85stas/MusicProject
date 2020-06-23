package stas.batura.musicproject.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.ResponseBody

@Dao
abstract class TracksDao {

    //----------------------------MAIN PART---------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setMainPlaylistId(mainData: MainData)

    @Query ("UPDATE main_table SET current_playlist_id =:playlistId")
    abstract fun updateMainPlayilistId(playlistId: Int)

    @Query("SELECT * FROM main_table WHERE mainId= :id")
    abstract fun getMainPlaylistId(id: Long): LiveData<MainData>

    //----------------------------TRAKS PART---------------------------------------------------------
    @Insert
    abstract fun insertTrack(trackKot: TrackKot)

    @Insert
    abstract fun insertTracks(tracks: List<TrackKot>)

    @Query ("SELECT * FROM tracks_table WHERE track_playlist_id = :playlistId ORDER BY id ASC")
    abstract fun getAllTracksFromPlaylist(playlistId: Int): List<TrackKot>?

    @Query ("SELECT * FROM tracks_table ORDER BY id")
    abstract fun getAllTracks(): List<TrackKot>?

    @Query ("SELECT * FROM tracks_table  WHERE track_playlist_id IN ( SELECT current_playlist_id FROM main_table) ORDER BY year ASC")
    abstract fun getAllTracksFromMainPlaylist(): List<TrackKot>?

    @Query ("UPDATE tracks_table SET is_playing = 1 WHERE id = :trackId AND track_playlist_id IN ( SELECT current_playlist_id FROM main_table) ")
    abstract fun setTrackIsPlaying(trackId: Int)

    @Query ("SELECT * FROM tracks_table WHERE is_playing = 1")
    abstract fun getPlayingTrack(): LiveData<TrackKot>

    @Query ("UPDATE tracks_table SET is_playing = 0")
    abstract fun setAllTrackIsNOTPlaying()

    @Query ("DELETE FROM tracks_table")
    abstract fun deleteAllTracks()

    //----------------------------PLAYLIST PART---------------------------------------------------------
    @Query ("DELETE FROM tracks_table WHERE track_playlist_id = :playlistId")
    abstract fun deleteTracksInPlayList(playlistId: Int)

    @Query ("DELETE FROM tracks_table WHERE track_playlist_id IN ( SELECT current_playlist_id FROM main_table)")
    abstract fun deleteTracksInMainPlayList()

    @Query ("DELETE FROM tracks_table WHERE id = :id")
    abstract fun deleteTrack(id: Int)

    @Insert
    abstract fun insertPlaylist(playlist: Playlist): Long

    @Query ("SELECT * FROM playlist_table ORDER BY playlist_id")
    abstract fun getAllPlaylists() : LiveData<List<Playlist>>

    @Query ("UPDATE playlist_table SET name= :name WHERE playlist_id = :playlistId")
    abstract fun updatePlaylistName(playlistId: Int, name: String)

    @Query("SELECT * FROM playlist_table WHERE playlist_id IN ( SELECT current_playlist_id FROM main_table)")
    abstract fun getCurrPlaylistName() : LiveData<Playlist?>

    @Query("DELETE FROM playlist_table WHERE playlist_id IN ( SELECT current_playlist_id FROM main_table)")
    abstract fun deletePlaylist()

    //----------------------------CONTROLS PART---------------------------------------------------------
    @Insert
    abstract fun addControls(controls: Controls)

    @Query ("UPDATE control_table SET playStatus = :status")
    abstract fun changerPlayStatus(status:Int)

//    @Query ("UPDATE control_table SET shuffleStaus = :status")
//    abstract fun changerShuffleStatus(status:Int)

    @Query("SELECT * FROM control_table")
    abstract fun getControls(): LiveData<Controls>

    //--------------------------retrofit part-----------------------------------------------------------
}