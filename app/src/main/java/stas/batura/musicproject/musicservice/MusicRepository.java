package stas.batura.musicproject.musicservice;

import android.net.Uri;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import stas.batura.musicproject.R;

//https://simpleguics2pygame.readthedocs.io/en/latest/_static/links/snd_links.html
public final class MusicRepository {



    private final Track[] data = {
            new Track(0,"Triangle", "Jason Shaw", R.drawable.image266680,                   Uri.parse("https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"), (3 * 60 + 41) * 1000,false),
            new Track(1,"Rubix Cube", "Jason Shaw", R.drawable.image396168,                 Uri.parse("https://codeskulptor-demos.commondatastorage.googleapis.com/descent/background%20music.mp3"), (3 * 60 + 44) * 1000, false),
            new Track(2,"MC Ballad S Early Eighties", "Frank Nora", R.drawable.image533998, Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-assets/sounddogs/soundtrack.mp3"), (2 * 60 + 50) * 1000, false),
            new Track(3,"Folk Song", "Brian Boyko", R.drawable.image544064,                 Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/lose.ogg"), (3 * 60 + 5) * 1000, false),
            new Track(4,"Morning Snowflake", "Kevin MacLeod", R.drawable.image208815,       Uri.parse("https://commondatastorage.googleapis.com/codeskulptor-demos/riceracer_assets/music/race1.ogg"), (2 * 60 + 0) * 1000, false),
    };

    public final List<Track> tracks = Arrays.asList(data);

    private final int maxIndex = tracks.size() - 1;
    private int currentItemIndex = 0;

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

    Track getCurrent() {
        return tracks.get(currentItemIndex);
    }

    public static class Track {
        public int trackId;
        public String title;
        private String artist;
        private int bitmapResId;
        private Uri uri;
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
