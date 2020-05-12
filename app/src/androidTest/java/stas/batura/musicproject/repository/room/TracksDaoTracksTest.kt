package stas.batura.musicproject.repository.room

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import stas.getOrAwaitValue
import java.io.File

class TracksDaoTracksTest {

    private lateinit var database: TracksDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //    private FakeRepositoryAndr fakeRepository = null;
    private var trackKot0Play1: TrackKot? = null
    private var trackKot1Play1: TrackKot? = null
    private var trackKot1Play2: TrackKot? = null
    private var trackKot2Play2: TrackKot? = null

    @Before
    fun initDb() {
        val uri =
            Uri.fromFile(File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"))
        val uriBit =
            Uri.fromFile(File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"))
        trackKot0Play1 = TrackKot(
            1, "title1", "artist1", "album1", uriBit, uri, 1000, "bit", 1985
        )
        trackKot1Play1 = TrackKot(
            1, "title2", "artist2", "album2", uriBit, uri, 1000, "bit", 1985
        )
        trackKot1Play2 = TrackKot(
            2, "title1", "artist1", "album1", uriBit, uri, 1000, "bit", 1985
        )
//        trackKot2Play2 = TrackKot(
////            2, "title2", "artist2", "album2", 0, uri, 1000, "bit", 1985
////        )
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TracksDatabase::class.java
        ).build()

        val playlist1 = Playlist("playlist1")
        database.tracksDatabaseDao.insertPlaylist(playlist1)
        val playlist2 = Playlist("playlist2")
        database.tracksDatabaseDao.insertPlaylist(playlist2)

        database.tracksDatabaseDao.insertTrack(trackKot0Play1!!)
        database.tracksDatabaseDao.insertTrack(trackKot1Play1!!)
        database.tracksDatabaseDao.insertTrack(trackKot1Play2!!)
        }

    @After
    fun closeDb() = database.close()

    @Test
    fun get_playlists() {

        val playlists = database.tracksDatabaseDao.getAllPlaylists().getOrAwaitValue()

        assertEquals (playlists.get(0).name, "playlist1")
        assertEquals (playlists.get(1).name, "playlist2")

        val mainData = MainData(1)
        database.tracksDatabaseDao.setMainPlaylistId(mainData)
        assertEquals(database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size, 2)

        val mainData1 = MainData(2)
        database.tracksDatabaseDao.setMainPlaylistId(mainData1)
        assertEquals(database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size, 1)
    }


    @Test
    fun tracks_from_mainPlaylist() {

        val mainData = MainData(2)
        database.tracksDatabaseDao.setMainPlaylistId(mainData)

        assertEquals(database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.get(0),
            trackKot1Play2)

        val mainData1 = MainData(1)
        database.tracksDatabaseDao.setMainPlaylistId(mainData1)

        assertEquals(database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.get(1),
            trackKot1Play1)
    }

    @Test
    fun delete_tracks_from_mainPlaylist() {
        val mainData1 = MainData(1)
        database.tracksDatabaseDao.setMainPlaylistId(mainData1)
        database.tracksDatabaseDao.deleteTracksInMainPlayList()
        assertEquals(0, database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size)

        val mainData2 = MainData(2)
        database.tracksDatabaseDao.setMainPlaylistId(mainData2)
        assertEquals(1, database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size)
    }

    @Test
    fun delete_tracks_fromPlaylist() {
        database.tracksDatabaseDao.deleteTracksInPlayList(2)

        val mainData1 = MainData(1)
        database.tracksDatabaseDao.setMainPlaylistId(mainData1)
        assertEquals(2, database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size)

        val mainData2 = MainData(2)
        database.tracksDatabaseDao.setMainPlaylistId(mainData2)
        assertEquals(0, database.tracksDatabaseDao.getAllTracksFromMainPlaylist()!!.size)
    }
}