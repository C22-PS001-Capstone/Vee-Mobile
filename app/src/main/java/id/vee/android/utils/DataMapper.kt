package id.vee.android.utils

import id.vee.android.data.local.entity.*
import id.vee.android.data.remote.response.ActivityResponse
import id.vee.android.data.remote.response.GasStationsResponse
import id.vee.android.domain.model.*

object DataMapper {
    fun mapEntityToDomain(input: UserEntity) = User(
        id = input.id,
        firstName = input.firstName,
        lastName = input.lastName,
        email = input.email
    )

    fun mapEntityToDomain(input: TokenEntity) = Token(
        accessToken = input.accessToken,
        refreshToken = input.refreshToken,
        expiredAt = input.expiredAt,
    )

    fun mapEntityToDomain(input: RoboEntity) = Robo(
        price = input.prc,
        liter = input.ltr
    )

    fun mapDomainToEntity(input: User) = UserEntity(
        id = input.id,
        firstName = input.firstName,
        lastName = input.lastName,
        email = input.email
    )

    fun mapDomainToEntity(input: Token) = TokenEntity(
        accessToken = input.accessToken,
        refreshToken = input.refreshToken,
        expiredAt = input.expiredAt,
    )

    fun mapResponsesToEntities(input: List<ActivityResponse>): List<ActivityEntity> {
        val list = mutableListOf<ActivityEntity>()
        input.map {
            list.add(
                ActivityEntity(
                    date = it.date,
                    owner = it.owner,
                    km = it.km,
                    price = it.price,
                    liter = it.liter,
                    lon = it.lon,
                    id = it.id,
                    lat = it.lat
                )
            )
        }
        return list
    }

    @JvmName("mapResponsesToEntities1")
    fun mapResponsesToEntities(input: List<GasStationsResponse>): List<GasStationsEntity> {
        val list = mutableListOf<GasStationsEntity>()
        input.map {
            list.add(
                GasStationsEntity(
                    id = it.id,
                    name = it.name,
                    vendor = it.vendor,
                    distance = it.distance,
                    lat = it.lat,
                    lon = it.lon
                )
            )
        }
        return list
    }

    fun mapEntitiesToDomain(input: List<ActivityEntity>): List<Activity> =
        input.map {
            Activity(
                date = it.date,
                owner = it.owner,
                km = it.km,
                price = it.price,
                liter = it.liter,
                lon = it.lon,
                id = it.id,
                lat = it.lat,
            )
        }

    @JvmName("mapEntitiesToDomain1")
    fun mapEntitiesToDomain(
        input: List<GasStationsEntity>
    ): List<GasStations> =
        input.map {
            GasStations(
                id = it.id,
                name = it.name,
                vendor = it.vendor,
                distance = it.distance,
                lat = it.lat,
                lon = it.lon
            )
        }

    @JvmName("mapEntitiesToDomain2")
    fun mapEntitiesToDomain(input: List<RoboEntity>): List<Robo> =
        input.map {
            Robo(
                price = it.price,
                liter = it.liter
            )
        }

    @JvmName("mapEntitiesToDomain3")
    fun mapEntitiesToDomain(input: List<NotificationEntity>): List<Notification> =
        input.map {
            Notification(
                id = it.id,
                notification = it.notification,
                createdAt = it.createdAt
            )
        }

    fun mapDomainToEntity(notification: Notification): NotificationEntity =
        NotificationEntity(
            id = null,
            notification = notification.notification,
            createdAt = notification.createdAt,
        )
}