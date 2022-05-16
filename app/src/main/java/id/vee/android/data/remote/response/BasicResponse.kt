package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class BasicResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Any?
)