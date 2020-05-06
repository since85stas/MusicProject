package stas.batura.musicproject.musicservice;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

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
import stas.batura.musicproject.utils.InjectorUtils;

//https://simpleguics2pygame.readthedocs.io/en/latest/_static/links/snd_links.html
public final class MusicRepository {

    private TracksDao repository;

    public MutableLiveData<List<Track>> tracks = new  MutableLiveData<>();

    public List<TrackKot> tracksDb;

    private int maxIndex = 20;

    private int currentItemIndex = 0;

    public MusicRepository(TracksDao repository ) {
        this.repository = repository;

        getDbTracks();
        System.out.println("end repos creat");
    }

    public void getDbTracks() {
        tracksDb = repository.getAllTracksFromMainPlaylist();
        updateTracksLive(tracksDb);
    }

    void updateTracksLive (List<TrackKot> trackKotList) {

        List<Track> tacksRep = new ArrayList<>();
        for (int i = 0; i < trackKotList.size(); i++) {
            Track track = new Track(trackKotList.get(i));
            tacksRep.add(track );
        }

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

    public void setPlayByUri(Uri uri) {
        currentItemIndex = getIndexByUri(uri);
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

    // TODO : разобраться с обновлением лайв дэйта
    private void setIsPlaying() {
        Log.d("musicReppos", "setIsPlaying: ");
        for (Track tr : tracks.getValue()) {
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
        public String bitrate;
        public int year;

        Track(int id, String title, String artist,String album, int bitmapResId, Uri uri, Long duration, boolean is,
              String bitrate, int year) {
            this.trackId = id;
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.bitmapResId = bitmapResId;
            this.uri = uri;
            this.duration = duration;
            this.isPlaying = is;
            this.bitrate = bitrate;
            this.year    = year;
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
            this.bitrate = trackKot.getBitrate();
            this.year    = trackKot.getYear();
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
                    duration.equals(track.duration) &&
                    title.equals(track.title) &&
                    artist.equals(track.artist) &&
                    uri.equals(track.uri) &&
                    isPlaying == track.isPlaying;
        }

        @Override
        public int hashCode() {
            return Objects.hash(trackId, title, artist, bitmapResId, uri, duration, isPlaying);
        }
    }
}
