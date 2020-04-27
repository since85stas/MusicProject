package stas.batura.musicproject.musicservice;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public static MusicRepository recreateMusicRepository(Application contex) {
        instance = null;
        return getInstance(contex);
    }

    private MusicRepository(Application contex ) {
        TracksDao tracksDao = TracksDatabase.Companion.getInstance(contex).getTracksDatabaseDao();
        repository = new Repository(tracksDao);

        getDbTracks();
//        tracksDb = repository.getAllTracks();
//        updateTracksLive(tracksDb);
        System.out.println("end repos creat");
    }

    public void getDbTracks() {
        tracksDb = repository.getAllTracksFromMainPlaylist();
//        traksAll = repository.get/AllTracks();
//        List<TrackKot> traksId = repository.getAllTracksFromPlaylist(4);
        updateTracksLive(tracksDb);
    }

    public MutableLiveData<List<Track>> tracks = new  MutableLiveData<>();

    public List<TrackKot> tracksDb;

//    public List<TrackKot> traksAll;

//    private final int maxIndex = tracks.getValue().size() - 1;
    private int maxIndex = 20;
    private int currentItemIndex = 0;

    void updateTracksLive (List<TrackKot> trackKotList) {

        List<Track> tacksRep = new ArrayList<>();
        for (int i = 0; i < trackKotList.size(); i++) {
            Track track = new Track(trackKotList.get(i));
            tacksRep.add(track );
        }

        tracks.setValue(tacksRep);
        tracks.postValue(tacksRep);
        maxIndex = tacksRep.size()-1;
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
                return i;
            }
        }
        return 0;
    }

//    public void addTrack () {
//        try {
//            List<Track> newList = new ArrayList<>(Arrays.asList(data));
//            newList.add(new Track(0, "Triangle", "Jason Shaw", "album" ,R.drawable.image266680, Uri.parse("https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"), (3 * 60 + 41) * 1000, false));
//            tracks.setValue(newList);
//        } catch (Exception e) {
//            System.out.println(" E " + e);
//        }
//    }

    // TODO : разобраться с обновлением лайв дэйта
    private void setIsPlaying() {
        for (Track tr :
                tracks.getValue()) {
            tr.isPlaying = false;
        }
        repository.setAllTrackIsNOTPlaying();
        repository.setTrackIsPlaying(tracks.getValue().get(currentItemIndex).trackId);
        getDbTracks();
//        tracks.getValue().get(currentItemIndex).isPlaying = true;
//        tracks.setValue(new ArrayList<>(tracks.getValue()));
    }

    public static class Track {
        public int trackId;
        public String title;
        private String artist;
        public String album;
        private int bitmapResId;
        public Uri uri;
        public Long duration; // in ms
        public boolean isPlaying;

        Track(int id, String title, String artist,String album, int bitmapResId, Uri uri, Long duration, boolean is) {
            this.trackId = id;
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.bitmapResId = bitmapResId;
            this.uri = uri;
            this.duration = duration;
            this.isPlaying = is;
        }

        Track(TrackKot trackKot) {
            this.trackId = trackKot.get_ID();
            this.title = trackKot.getTitle();
            this.artist = trackKot.getArtist();
            this.album = trackKot.getAlbum();
            this.bitmapResId = trackKot.getBitmapResId();
            this.uri = trackKot.getUri();
            this.duration = trackKot.getDuration();
            this.isPlaying = trackKot.isPlaying();
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

        Long getDuration() {
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
