package id.vee.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GasStations(
    val id: String,
    val name: String,
    val vendor: String,
    val distance: Double = 0.0,
    val lat: Double,
    val lon: Double
) : Parcelable
