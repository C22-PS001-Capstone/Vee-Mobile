package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName
import id.vee.android.data.local.entity.TokenEntity

data class LoginResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: TokenEntity?
)