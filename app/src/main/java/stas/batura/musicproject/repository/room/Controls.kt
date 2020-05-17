package stas.batura.musicproject.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

val CONTR_ID = 34

@JvmField val REPEAT_ON = 23
@JvmField val REPEAT_OFF = 24
@JvmField val REPEAT_ONE = 25

@JvmField val SHUFFLE_ON = 34
//@JvmField val SHUFFLE_OFF = 35

@Entity (tableName = "control_table")
data class Controls (
    var playStatus: Int = 0
//    var shuffleStaus: Int = 0
)

{
    @PrimaryKey
    var _Id = CONTR_ID
}