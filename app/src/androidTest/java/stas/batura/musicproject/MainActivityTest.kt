package stas.batura.musicproject

import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import stas.batura.musicproject.repository.Repository
import stas.batura.musicproject.repository.room.MainData
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.utils.InjectorUtils
import java.io.File
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var repository: TracksDao

    private var trackKot0: TrackKot? = null
    private var trackKot1: TrackKot? = null
    private var trackKot2: TrackKot? = null

    @Before
    fun init() {
        repository = InjectorUtils.provideRep(ApplicationProvider.getApplicationContext())
        val uri =
            Uri.fromFile(File("file:///storage/emulated/0/Download/08-Filosofem%201996/01-Dunkelheit.mp3"))
        val uriBit =
            Uri.fromFile(File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"))

        trackKot0 = TrackKot(1, "Dunkelheit", "Burzum", "Filosofem", uriBit, uri, 425096, "192000", 1996)
//        trackKot1 = TrackKot(1, "title2", "artist2", "album2", 0, uri, 1000, "bit", 1985)
//        trackKot2 = TrackKot(1, "title3", "artist3", "album3", 0, uri, 1000, "bit", 1985)

        repository.insertTrack(trackKot0!!)
//        repository.insertTrack(trackKot1!!)
        val playlist = Playlist("playlist")
        repository.insertPlaylist(playlist)

        val mainData = MainData(1)
        repository.setMainPlaylistId(mainData)

    }


    @Test
    fun test_activity_decor_init() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.albumTitle)).check(matches(withText("Album")))
        onView(withId(R.id.songTitle)).check(matches(withText("Title")))

        activityScenario.close()
    }

    @Test
    fun test_activity_play_clicked_decor() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.play_button)).perform(click())
        onView(withId(R.id.albumTitle)).check(matches(withText("Filosofem")))
        activityScenario.close()
    }

    @Test
    fun test_view_pager_view() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(allOf(withId(R.id.albumTitle))).check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.pager)).perform(swipeLeft())
        onView(allOf(withId(R.id.add_files_butt))).check(matches(isCompletelyDisplayed()))

//        onView(withId(R.id.pager)).perform(swipeRight())
//        onView(allOf(withId(R.id.albumTitle))).check(matches(isCompletelyDisplayed()))

        activityScenario.close()
    }

    @After
    fun reset() {
        InjectorUtils.resetRepository()
    }

}