package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityListResponse(

    @field:SerializedName("data")
    val data: ActivityListDataResponse,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class ActivityListDataResponse(

    @field:SerializedName("activities")
    val activities: List<ActivityResponse>
)