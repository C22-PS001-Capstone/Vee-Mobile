package id.vee.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Robo(
    val price: Int,
    val liter: Int
) : Parcelable