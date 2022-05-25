package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class GasStationsResponse(

    @field:SerializedName("data")
    val data: GasStationsDataResponse? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class GasStationsDataResponse(

    @field:SerializedName("activities")
    val activities: List<GasStationsResponse>
)