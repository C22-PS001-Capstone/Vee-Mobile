package id.vee.android.domain.model

data class Settings(
    var theme: Boolean = false,
    var saverMode: Boolean = true,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)