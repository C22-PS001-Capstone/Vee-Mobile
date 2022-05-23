package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityListResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class Data(

    @field:SerializedName("activities")
    val activities: List<ActivityResponse>
)