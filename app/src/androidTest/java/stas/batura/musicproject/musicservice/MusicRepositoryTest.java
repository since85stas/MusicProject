package stas.batura.musicproject.musicservice;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import stas.batura.musicproject.data.source.FakeRepositoryAndr;
import stas.batura.musicproject.repository.room.TrackKot;
import stas.batura.musicproject.utils.InjectorUtils;

import static org.junit.Assert.*;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class MusicRepositoryTest {

    private static final String TAG = "testMusRep";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

//    @Rule
//    private  instantExecutorRule = new InstantTaskExecutorRule();

    private MusicRepository musicRepository = null;
//    private FakeRepositoryAndr fakeRepository = null;

    private TrackKot trackKot0;
    private TrackKot trackKot1;
    private TrackKot trackKot2;

    private MusicRepository.Track track0;
    private MusicRepository.Track track1;
    private MusicRepository.Track track2;

    @Before
    public void setUp() throws Exception {
        Log.d(TAG, "setUp: ");
        FakeRepositoryAndr fakeRepository = new FakeRepositoryAndr();
        Uri uri = Uri.fromFile(new File("/mnt/sdcard/Music/red elvises/The Best of Kick-Ass"));
        trackKot0 = new TrackKot(0,"title1","artist1","album1",0,uri,1000,"bit",1985
        );
        trackKot1 = new TrackKot(0,"title2","artist2","album2",0,uri,1000,"bit",1985
        );
        trackKot2 = new TrackKot(0,"title3","artist3","album3",0,uri,1000,"bit",1985
        );
        fakeRepository.insertTrack(trackKot0);
        fakeRepository.insertTrack(trackKot1);
        fakeRepository.insertTrack(trackKot2);
        InjectorUtils.INSTANCE.setRepository(fakeRepository);

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
//        Application app = InstrumentationRegistry.getInstrumentation().newApplication(Application, context);

        musicRepository = InjectorUtils.INSTANCE.provideMusicRep(context);
    }

    @After
    public void tearDown() throws Exception {
        Log.d(TAG, "tearDown: ");
        InjectorUtils.INSTANCE.resetRepository();
    }

    @Test
    public void test_tracksDb_init_value() {
        List<TrackKot> tracks = musicRepository.tracksDb;
        assertEquals(tracks.get(0), trackKot0);
        assertEquals(tracks.get(2),(trackKot2));
    }

    @Test
    public void test_tracks_getters() {
        MusicRepository.Track track = musicRepository.getCurrent();
        MusicRepository.Track trackQ = new MusicRepository.Track(trackKot0);
        assertEquals(track,trackQ);
    }

    @Test
    public void test_tracks_by_id() {
        assertEquals (musicRepository.getTrackByIndex(0),(new MusicRepository.Track(trackKot0)));
        assertEquals (musicRepository.getTrackByIndex(1),(new MusicRepository.Track(trackKot1)));
        assertEquals (musicRepository.getTrackByIndex(2),(new MusicRepository.Track(trackKot2)));
    }

    @Test
    public void test_get_next_prev_fun() {
        assertEquals (musicRepository.getNext(),(new MusicRepository.Track(trackKot1)));
        assertEquals (musicRepository.getPrevious(),(new MusicRepository.Track(trackKot0)));
    }

    @Test
    public void test_getPrevious_first() {
        assertEquals (musicRepository.getPrevious(),(new MusicRepository.Track(trackKot2)));
    }

    @Test
    public void test_get_next_2_fun() {
        musicRepository.getNext();
        musicRepository.getNext();
        assertEquals (musicRepository.getNext(),(new MusicRepository.Track(trackKot0)));
    }
}