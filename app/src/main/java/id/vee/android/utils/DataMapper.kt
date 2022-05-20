package id.vee.android.utils

import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
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
}