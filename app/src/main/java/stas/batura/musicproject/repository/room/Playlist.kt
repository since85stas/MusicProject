package stas.batura.musicproject.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class Playlist (
    @ColumnInfo(name = "name")
    val name: String = "playlist"
) {
    @ColumnInfo(name = "playlist_id")
    @PrimaryKey(autoGenerate = true)
    private val playlistId = 0
}