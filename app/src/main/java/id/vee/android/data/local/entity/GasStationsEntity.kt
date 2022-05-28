package id.vee.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "gas_stations_entities", primaryKeys = ["id"])
data class GasStationsEntity(
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "vendor")
    val vendor: String?,

    @ColumnInfo(name = "distance")
    val distance: String?,

    @ColumnInfo(name = "lat")
    val lat: Double = 0.0,

    @ColumnInfo(name = "lon")
    val lon: Double = 0.0
)