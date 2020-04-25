package stas.batura.musicproject.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class Playlist (
    @ColumnInfo(name = "name")
    var name: String = "playlist"
) {
    @ColumnInfo(name = "playlist_id")
    @PrimaryKey(autoGenerate = true)
    var playlistId : Int = 0
}