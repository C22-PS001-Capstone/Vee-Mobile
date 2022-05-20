package id.vee.android.domain.model

data class Token(
    var accessToken: String,
    var refreshToken: String,
    var expiredAt: Long,
)