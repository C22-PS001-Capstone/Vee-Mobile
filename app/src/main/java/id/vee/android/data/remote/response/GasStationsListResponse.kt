package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class GasStationsListResponse(

    @field:SerializedName("data")
    val data: List<GasStationsResponse>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)
