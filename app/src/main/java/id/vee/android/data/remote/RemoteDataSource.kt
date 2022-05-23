package id.vee.android.data.remote

import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.remote.network.ApiService
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.utils.bearer

class RemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse {
        return try {
            apiService.signup(
                firstName,
                lastName,
                email,
                password,
                passwordConfirm
            )
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun login(username: String, password: String): LoginResponse {
        return try {
            apiService.login(username, password)
        } catch (e: Exception) {
            LoginResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun refreshToken(refreshToken: String): LoginResponse {
        return try {
            apiService.refreshToken(refreshToken)
        } catch (e: Exception) {
            LoginResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun userDetail(data: TokenEntity): UserDetailResponse {
        return try {
            apiService.userDetail(data.accessToken.bearer())
        } catch (e: Exception) {
            UserDetailResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Int,
        expense: Int,
        lat: Double,
        long: Double
    ): BasicResponse {
        return try {
            apiService.insertActivity(token.bearer(), date, distance, litre, expense, lat, long)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun logout(token: String): BasicResponse {
        return try {
            apiService.logout(token)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun updateName(
        token: String,
        firstName: String,
        lastName: String
    ): BasicResponse {
        return try {
            apiService.updateName(token.bearer(), firstName, lastName)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun updatePassword(
        token: String,
        passwordCurrent: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse {
        return try {
            apiService.updatePassword(token.bearer(), passwordCurrent, password, passwordConfirm)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }
}