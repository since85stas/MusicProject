package stas.batura.musicproject.repository.room

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import stas.getOrAwaitValue
import java.io.File


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TracksDaoTest {

    private lateinit var database: TracksDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //    private FakeRepositoryAndr fakeRepository = null;
    private var trackKot0: TrackKot? = null
    private var trackKot1: TrackKot? = null
    private var trackKot2: TrackKot? = null

    @Before
    fun initDb() {
        val uri =
            Uri.fromFile(File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"))

        val uriBit =
            Uri.fromFile(File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"))
        trackKot0 = TrackKot(
            0, "title1", "artist1", "album1", uriBit, uri, 1000, "bit", 1985
        )
        trackKot1 = TrackKot(
            0, "title2", "artist2", "album2", uriBit, uri, 1000, "bit", 1985
        )
        trackKot2 = TrackKot(
            0, "title3", "artist3", "album3", uriBit, uri, 1000, "bit", 1985
        )
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TracksDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun main_data_insert_and_get_test() {
        val playlist = Playlist("playlist")
        database.tracksDatabaseDao.insertPlaylist(playlist)

        val mainData = MainData(1)
        database.tracksDatabaseDao.setMainPlaylistId(mainData)

        assertEquals(database.tracksDatabaseDao.getMainPlaylistId(0L).getOrAwaitValue().currentPlaylistId,
            mainData.currentPlaylistId)
    }

    @Test
    fun track_insert_and_get() {
        database.tracksDatabaseDao.insertTrack(trackKot0!!)
        database.tracksDatabaseDao.insertTrack(trackKot1!!)

        val getTrack = database.tracksDatabaseDao.getAllTracks()

        assertEquals(getTrack!![0],trackKot0)
        assertEquals(getTrack!![1],trackKot1)
    }





}