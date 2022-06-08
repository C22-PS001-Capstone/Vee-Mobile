package id.vee.android

import id.vee.android.domain.model.Token

object DataDummy {
    fun getTokenData(): Token =
        Token(
            "accessToken",
            "refreshToken",
            1654702049
        )
}