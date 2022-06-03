package id.vee.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    val date: String,
    val owner: String,
    val km: Int,
    val price: Int,
    val liter: Int,
    val lon: Double,
    val id: String,
    val lat: Double,
    var isMonthShow: Boolean = false
) : Parcelable
