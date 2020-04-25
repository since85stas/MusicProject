package stas.batura.musicproject.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

val MAIN_ID = 23;

@Entity(tableName = "playlist_table")
data class Playlist (

    @ColumnInfo(name = "name")
    var name: String = "playlist"
) {
    @ColumnInfo(name = "playlist_id")
    @PrimaryKey(autoGenerate = true)
    var playlistI:Int = MAIN_ID
}