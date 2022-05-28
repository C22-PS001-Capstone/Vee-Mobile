package id.vee.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("distance")
	val distance: String? = null,

	@field:SerializedName("vendor")
	val vendor: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)
