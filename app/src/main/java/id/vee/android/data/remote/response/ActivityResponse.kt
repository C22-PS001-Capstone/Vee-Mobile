package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("owner")
    val owner: String,

    @field:SerializedName("km")
    val km: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("liter")
    val liter: Double,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Double
)