package id.vee.android.domain.model

data class Settings(
    var theme: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)