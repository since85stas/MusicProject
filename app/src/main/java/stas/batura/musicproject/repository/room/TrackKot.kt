package stas.batura.musicproject.repository.room

import android.net.Uri
import androidx.annotation.ColorInt
import androidx.room.*

@Entity (tableName = "tracks_table")
data class TrackKot (
    @ColumnInfo(name = "track_playlist_id")
    var trackId: Int = 0,

    @ColumnInfo(name = "title")
    var title: String = "title",

    @ColumnInfo(name = "artist")
    var artist: String = "artist",

    @ColumnInfo(name = "album")
    var album: String = "album",

    @ColumnInfo(name = "bitmap_res_id")
    var bitmapResId : Int = 0,

    @ColumnInfo(name = "uri")
    var uri: Uri? = null,

    @ColumnInfo(name = "duration")
    var duration // in ms
            : Long = 0

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var _ID: Long = 0

    @ColumnInfo(name = "is_playing")
    var isPlaying = false
}

class UriRoomConverter {

    @TypeConverter
    fun fromUriToStr (uri: Uri) : String{
        return uri.toString()
    }

    @TypeConverter
    fun fromStrToUri (string: String) : Uri{
        return Uri.parse(string)
    }

}
