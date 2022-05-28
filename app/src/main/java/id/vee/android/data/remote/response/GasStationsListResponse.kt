package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class GasStationsListResponse(

    @field:SerializedName("data")
    val data: GasStationsDataResponse,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class GasStationsDataResponse(

    @field:SerializedName("gasstations")
    val gasStations: List<GasStationsResponse>
)