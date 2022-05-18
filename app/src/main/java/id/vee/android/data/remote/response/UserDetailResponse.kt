package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName
import id.vee.android.data.local.entity.UserEntity

data class UserDetailResponse(

    @SerializedName("data")
    val data: UserDetailDataResponse? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,
)

data class UserDetailDataResponse(

    @field:SerializedName("user")
    val user: UserEntity? = null
)