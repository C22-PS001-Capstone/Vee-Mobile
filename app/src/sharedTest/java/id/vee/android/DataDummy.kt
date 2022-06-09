package id.vee.android

import android.location.Location
import id.vee.android.data.local.entity.*
import id.vee.android.data.remote.response.*
import id.vee.android.domain.model.*

object DataDummy {
    fun getTokenData(): Token =
        Token(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            expiredAt = 1654702049
        )

    fun getTokenEntity(): TokenEntity =
        TokenEntity(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            expiredAt = 1654702049
        )

    fun getUserData(): User =
        User(
            id = "id-user-2",
            firstName = "first-2",
            lastName = "last-2",
            email = "email@mail.com",
            passNull = false
        )

    fun getUserEntity(): UserEntity =
        UserEntity(
            id = "id-user-2",
            firstName = "first-2",
            lastName = "last-2",
            email = "email@mail.com",
            passNull = false
        )

    fun getUserEntities(): List<UserEntity> {
        val list: MutableList<UserEntity> = mutableListOf()
        for (i in 0..10) {
            val user = UserEntity(
                id = "id-user-$i",
                firstName = "first-$i",
                lastName = "last-$i",
                email = "email@mail.com",
                passNull = false
            )
            list.add(user)
        }
        return list
    }

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

    fun gasStationsEntity(): List<GasStationsEntity> {
        val list: MutableList<GasStationsEntity> = mutableListOf()
        for (i in 0 until 10) {
            val gasStation = GasStationsEntity(
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


    fun gasStationsResponse(): List<GasStationsResponse> {
        val list: MutableList<GasStationsResponse> = mutableListOf()
        for (i in 0 until 10) {
            val gasStation = GasStationsResponse(
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

    fun notificationData(): Notification =
        Notification(
            id = null,
            notification = "notification-1",
            createdAt = 2154702049,
        )

    fun notificationsEntity(): NotificationEntity = NotificationEntity(
        id = null,
        notification = "notification-1",
        createdAt = 2154702049,
    )

    fun listNotificationEntities(): List<NotificationEntity>{
        val list: MutableList<NotificationEntity> = mutableListOf()
        for (i in 0 until 10) {
            val notification = NotificationEntity(
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

    fun getRobo(): Robo = Robo(
        price = 1000,
        liter = 12
    )

    fun forecastData(): Forecast = Forecast(
        forecast = listOf(1.31, 11.2, 3323.1, 12223.3)
    )

    fun forecastResponseData(): ForecastResponse = ForecastResponse(
        status = "success",
        message = "Success",
        data = forecastData()
    )

    fun getListResponse(): List<ActivityResponse> {
        val list: MutableList<ActivityResponse> = mutableListOf()
        for (i in 0 until 10) {
            val activity = ActivityResponse(
                id = "id-$i",
                date = "2020-01-01",
                owner = "owner-$i",
                km = i * 100,
                liter = i * 10,
                price = i * 1000,
                lon = 106.8232323,
                lat = -6.2233232,
            )
            list.add(activity)
        }
        return list
    }
    fun getActivities(): List<Activity> {
        val list: MutableList<Activity> = mutableListOf()
        for (i in 0 until 10) {
            val activity = Activity(
                id = "id-$i",
                date = "2020-01-01",
                owner = "owner-$i",
                km = i * 100,
                liter = i * 10,
                price = i * 1000,
                lon = 106.8232323,
                lat = -6.2233232,
            )
            list.add(activity)
        }
        return list
    }

    fun getListEntityResponse(): List<ActivityEntity> {
        val list: MutableList<ActivityEntity> = mutableListOf()
        for (i in 0 until 10) {
            val activity = ActivityEntity(
                id = "id-$i",
                date = "2020-01-01",
                owner = "owner-$i",
                km = i * 100,
                liter = i * 10,
                price = i * 1000,
                lon = 106.8232323,
                lat = -6.2233232,
            )
            list.add(activity)
        }
        return list
    }

    fun getActivityListResponse(): ActivityListResponse = ActivityListResponse(
        ActivityListDataResponse(
            activities = getListResponse()
        ),
        message = "Success",
        status = "success"
    )

    fun getGasStationsListResponse(): GasStationsListResponse = GasStationsListResponse(
        data = gasStationsResponse(),
        message = "Success",
        status = "success"
    )

    fun getForecastResponse(): ForecastResponse = ForecastResponse(
        status = "success",
        message = "Success",
        data = forecastData()
    )

    fun getRoboEntity(): RoboEntity = RoboEntity(
        prc = 1000,
        ltr = 12
    )
}