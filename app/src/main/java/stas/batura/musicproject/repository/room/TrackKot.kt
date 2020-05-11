package stas.batura.musicproject.repository.room

import android.net.Uri
import androidx.annotation.ColorInt
import androidx.room.*

@Entity (tableName = "tracks_table")
data class TrackKot (
    @ColumnInfo(name = "track_playlist_id")
    var trackPlaylistId: Int = 0,

    @ColumnInfo(name = "title")
    var title: String = "title",

    @ColumnInfo(name = "artist")
    var artist: String = "artist",

    @ColumnInfo(name = "album")
    var album: String = "album",

    @ColumnInfo(name = "bitmap_uri")
    var bitmapUri : Uri? = null,

    @ColumnInfo(name = "uri")
    var uri: Uri? = null,

    @ColumnInfo(name = "duration")
    var duration: Long = 0L,

    @ColumnInfo(name = "bitrate")
    var bitrate: String = "",

    @ColumnInfo(name = "year")
    var year: Int = 0


) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var _ID: Int = 0

    @ColumnInfo(name = "is_playing")
    var isPlaying = false
}

class UriRoomConverter {

    @TypeConverter
    fun fromUriToStr (uri: Uri?) : String?{
        if (uri==null) return null
        return uri.toString()
    }

    @TypeConverter
    fun fromStrToUri (string: String?) : Uri?{
        if (string==null) return null
        return Uri.parse(string)
    }

}
