package stas.batura.musicproject.repository.room;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import stas.batura.musicproject.musicservice.MusicRepository;

@Entity (tableName = "tracks_table")
public class Track {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long _ID;

    @ColumnInfo (name = "track_playlist_id")
    public int trackId;

    @ColumnInfo (name = "title")
    public String title;

    @ColumnInfo (name = "artist")
    public String artist;

    @ColumnInfo (name = "bitmap_res_id")
    public int bitmapResId;

    @ColumnInfo (name = "uri")
    public Uri uri;

    @ColumnInfo (name = "duration")
    public long duration; // in ms

    @ColumnInfo (name = "is_playing")
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
