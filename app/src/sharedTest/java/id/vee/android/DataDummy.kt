package id.vee.android

import android.location.Location
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailDataResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.*

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

    fun gasStations(): List<GasStations> {
        val list: MutableList<GasStations> = mutableListOf()
        for (i in 0 until 10) {
            val gasStation = GasStations(
                id = "id-$i",
                name = "name-$i",
                vendor = "vendor-$i",
                lat = -6.2233232,
                lon = 106.8232323,
                distance = "${i}00",
            )
            list.add(gasStation)
        }
        return list
    }

    fun notificationsData(): List<Notification> {
        val list: MutableList<Notification> = mutableListOf()
        for (i in 0 until 10) {
            val notification = Notification(
                id = i + 1,
                notification = "notification-$i",
                createdAt = 2154702049,
            )
            list.add(notification)
        }
        return list
    }

    fun getLoginResponse(): LoginResponse =
        LoginResponse(
            status = "success",
            message = "Login Success",
            data = TokenEntity(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiredAt = 1654702049
            ),
        )

    fun getUserDetail(): UserDetailResponse =
        UserDetailResponse(
            status = "success",
            message = "Login Success",
            data = UserDetailDataResponse(
                UserEntity(
                    id = "id-user",
                    firstName = "first",
                    lastName = "last",
                    email = "mail@mail.com",
                    passNull = false
                )
            )
        )

    fun getBasicResponse(): BasicResponse =
        BasicResponse(
            status = "success",
            message = "Success",
            data = null
        )
}