package id.vee.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "robo_entities", primaryKeys = ["price"])
data class RoboEntity(
    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "liter")
    val liter: Int
)