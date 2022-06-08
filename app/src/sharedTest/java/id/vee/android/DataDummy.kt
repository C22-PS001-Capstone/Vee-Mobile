package id.vee.android

import android.location.Location
import id.vee.android.domain.model.Settings
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User

object DataDummy {
    fun getTokenData(): Token =
        Token(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            expiredAt = 1654702049
        )

    fun getUserData(): User =
        User(
            id = "id-user",
            firstName = "first",
            lastName = "last",
            email = "email@mail.com",
            passNull = false
        )

    fun getSettings(): Settings =
        Settings(
            theme = false,
            saverMode = false,
            latitude = -6.2233232,
            longitude = 106.8232323
        )

    fun getLiveLocation(): Location =
        Location("Live").apply {
            latitude = -6.2233232
            longitude = 106.8232323
        }
}