package id.vee.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "robo_entities", primaryKeys = ["price"])
data class RoboEntity(
    @ColumnInfo(name = "prc")
    val price: Int,

    @ColumnInfo(name = "ltr")
    val liter: Int
)