package stas.batura.musicproject.musicservice;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.io.File;

import stas.batura.musicproject.R;
import stas.batura.musicproject.repository.Repository;
import stas.batura.musicproject.repository.room.TrackKot;
import stas.batura.musicproject.repository.room.TracksDao;
import stas.batura.musicproject.repository.room.TracksDatabase;

//https://simpleguics2pygame.readthedocs.io/en/latest/_static/links/snd_links.html
public final class MusicRepository {

    private static MusicRepository instance;

    private Repository repository;

    public static MusicRepository getInstance(Application contex) {
        if (instance == null) {
            instance = new MusicRepository(contex);
            return instance;
        } else {

            return instance;
        }
    }

    private MusicRepository(Application contex ) {
        TracksDao tracksDao = TracksDatabase.Companion.getInstance(contex).getTracksDatabaseDao();
        repository = new Repository(tracksDao);
        tracksDb = repository.getAllTracks();
    }

    public void setData(File file) {
        data = new Track[1];

        data[0] =    new Track(0,"Triangle",
                "Jason Shaw",
                R.drawable.image266680,
                Uri.fromFile(file),
                (3 * 60 + 41) * 1000,false);

        updateTracksLive();
    }

    private Track[] data = new Track[0];
//            = {
//            new Track(0,"Triangle",
//                    "Jason Shaw",
//                    R.drawable.image266680,
//                    Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
//                            "/Music/Moonspell/Studio and Compilation/1995-Wolfheart (Original 1CD Release)/02 Love Crimes.mp3")),
//                    (3 * 60 + 41) * 1000,false),
//            new Track(1,"Rubix Cube", "Jason Shaw", R.drawable.image396168,                 Uri.parse("https://codeskulptor-demos.commondatastorage.googleapis.com/descent/background%20music.mp3"), (3 * 60 + 44) * 1000, false),
//            new Track(2,"MC Ballad S Early Eighties", "Frank Nora", R.drawable.image533998, Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-assets/sounddogs/soundtrack.mp3"), (2 * 60 + 50) * 1000, false),
//            new Track(3,"Folk Song", "Brian Boyko", R.drawable.image544064,                 Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/lose.ogg"), (3 * 60 + 5) * 1000, false),
//            new Track(4,"Morning Snowflake", "Kevin MacLeod", R.drawable.image208815,       Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/race1.ogg"), (2 * 60 + 0) * 1000, false),
//    };

    public MutableLiveData<List<Track>> tracks = new  MutableLiveData<>(Arrays.asList(data));

    public LiveData<List<TrackKot>> tracksDb;

    private final int maxIndex = tracks.getValue().size() - 1;
    private int currentItemIndex = 0;

    void updateTracksLive () {
        tracks = new MutableLiveData<>(Arrays.asList(data));
    }

    Track getNext() {
        if (currentItemIndex == maxIndex)
            currentItemIndex = 0;
        else
            currentItemIndex++;
        return getCurrent();
    }

    Track getPrevious() {
        if (currentItemIndex == 0)
            currentItemIndex = maxIndex;
        else
            currentItemIndex--;
        return getCurrent();
    }

    Track getTrackByIndex(int index) {
        currentItemIndex = index;
        return getCurrent();
    }

    Track getTrackByUri(Uri uri) {
        if (uri != null) {
            currentItemIndex = getIndexByUri(uri);
            return getCurrent();
        }
        return tracks.getValue().get(0);
    }

    Track getCurrent() {
        setIsPlaying();
        return tracks.getValue().get(currentItemIndex);
    }

    // получаем номер по uri
    private int getIndexByUri (Uri uri) {
        for (int i = 0; i < tracks.getValue().size(); i++) {
            if (tracks.getValue().get(i).uri.equals(uri)) {
                return tracks.getValue().get(i).trackId;
            }
        }
        return 0;
    }

    public void addTrack () {
        try {
            List<Track> newList = new ArrayList<>(Arrays.asList(data));
            newList.add(new Track(0, "Triangle", "Jason Shaw", R.drawable.image266680, Uri.parse("https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"), (3 * 60 + 41) * 1000, false));
            tracks.setValue(newList);
        } catch (Exception e) {
            System.out.println(" E " + e);
        }
    }

    // TODO : разобраться с обновлением лайв дэйта
    private void setIsPlaying() {
        for (Track tr :
                tracks.getValue()) {
            tr.isPlaying = false;
        }
        tracks.getValue().get(currentItemIndex).isPlaying = true;
        tracks.setValue(new ArrayList<>(tracks.getValue()));
    }

    public static class Track {
        public int trackId;
        public String title;
        private String artist;
        private int bitmapResId;
        public Uri uri;
        private long duration; // in ms
        public boolean isPlaying;

        Track(int id, String title, String artist, int bitmapResId, Uri uri, long duration, boolean is) {
            this.trackId = id;
            this.title = title;
            this.artist = artist;
            this.bitmapResId = bitmapResId;
            this.uri = uri;
            this.duration = duration;
            this.isPlaying = is;
        }

        String getTitle() {
            return title;
        }

        String getArtist() {
            return artist;
        }

        int getBitmapResId() {
            return bitmapResId;
        }

        Uri getUri() {
            return uri;
        }

        long getDuration() {
            return duration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Track track = (Track) o;
            return trackId == track.trackId &&
                    bitmapResId == track.bitmapResId &&
                    duration == track.duration &&
                    title.equals(track.title) &&
                    artist.equals(track.artist) &&
                    uri.equals(track.uri) &&
                    isPlaying == track.isPlaying
                    ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(trackId, title, artist, bitmapResId, uri, duration, isPlaying);
        }
    }
}
