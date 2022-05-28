package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class GasStationsResponse(
    @field:SerializedName("distance")
    val distance: String? = null,

    @field:SerializedName("vendor")
    val vendor: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Double? = null
)
