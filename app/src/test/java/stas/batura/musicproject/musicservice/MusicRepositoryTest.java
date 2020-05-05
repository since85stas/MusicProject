package stas.batura.musicproject.musicservice;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import stas.batura.musicproject.R;
import stas.batura.musicproject.data.source.FakeRepository;
import stas.batura.musicproject.repository.room.TrackKot;
import stas.batura.musicproject.repository.room.TracksDao;
import stas.batura.musicproject.utils.InjectorUtils;

import static org.junit.Assert.*;

public class MusicRepositoryTest {

    @Rule
    private InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private MusicRepository musicRepository = null;
    private FakeRepository fakeRepository = null;

    @Before
    public void setUp() throws Exception {
        fakeRepository = new FakeRepository();
        TrackKot track1 = new TrackKot(0,"title1","artist1","album1",0,null,1000,"bit",1985
        );
        TrackKot track2 = new TrackKot(0,"title2","artist2","album2",0,null,1000,"bit",1985
        );
        TrackKot track3 = new TrackKot(0,"title3","artist3","album3",0,null,1000,"bit",1985
        );
        fakeRepository.insertTrack(track1);
        fakeRepository.insertTrack(track2);
        fakeRepository.insertTrack(track3);
        InjectorUtils.INSTANCE.setRepository(fakeRepository);
    }

    @After
    public void tearDown() throws Exception {

    }


}