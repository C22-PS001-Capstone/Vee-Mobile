package id.vee.android.domain.model

data class Activity(
    val date: String,
    val owner: String,
    val km: Int,
    val price: Int,
    val liter: Int,
    val lon: Double,
    val id: String,
    val lat: Double
)
