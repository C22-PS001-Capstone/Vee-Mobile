package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName
import id.vee.android.domain.model.Forecast

data class ForecastResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Forecast?
)