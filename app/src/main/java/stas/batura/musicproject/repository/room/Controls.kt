package stas.batura.musicproject.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

val CONTR_ID = 34

val REPEAT_ON = 23
val REPEAT_OFF = 24
val REPEAT_ONE = 25

val SHUFFLE_ON = 34
val SHUFFLE_OFF = 35

@Entity (tableName = "control_table")
data class Controls (
    var repeatStatus: Int = 0,
    var shuffleStaus: Int = 0
)

{
    @PrimaryKey
    var _Id = CONTR_ID
}