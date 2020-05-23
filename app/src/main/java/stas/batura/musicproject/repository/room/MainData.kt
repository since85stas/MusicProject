package stas.batura.musicproject.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "main_table"
)
data class MainData(
    @ColumnInfo(name = "current_playlist_id")
    var currentPlaylistId: Int = -1
) {
    @PrimaryKey
    var mainId: Long = 0L
}