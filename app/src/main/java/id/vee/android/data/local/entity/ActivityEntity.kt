package id.vee.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "activity_entities", primaryKeys = ["id"])
data class ActivityEntity(
    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "owner")
    val owner: String,

    @ColumnInfo(name = "km")
    val km: Int,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "liter")
    val liter: Int,

    @ColumnInfo(name = "lon")
    val lon: Double,

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "lat")
    val lat: Double
)