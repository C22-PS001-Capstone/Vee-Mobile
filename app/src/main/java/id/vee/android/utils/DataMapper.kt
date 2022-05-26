package id.vee.android.utils

import id.vee.android.data.local.entity.ActivityEntity
import id.vee.android.data.local.entity.GasStationsEntity
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.ActivityResponse
import id.vee.android.data.remote.response.GasStationsResponse
import id.vee.android.domain.model.Activity
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User

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
    fun mapEntitiesToDomain(input: List<GasStationsEntity>): List<GasStations> =
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
}