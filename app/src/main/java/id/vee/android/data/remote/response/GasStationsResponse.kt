package id.vee.android.data.remote.response

import androidx.room.ColumnInfo

data class GasStationsResponse(
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "vendor")
    val vendor: String,

    @ColumnInfo(name = "distance")
    val distance: Double = 0.0,

    @ColumnInfo(name = "lat")
    val lat: Double,

    @ColumnInfo(name = "lon")
    val lon: Double
)
