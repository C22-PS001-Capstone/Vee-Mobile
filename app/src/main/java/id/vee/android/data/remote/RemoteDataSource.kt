package id.vee.android.data.remote

import android.util.Log
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.remote.network.ApiService
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.utils.bearer

class RemoteDataSource private constructor(
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
            Log.d(TAG, "signup: $e")
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

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }

        private const val TAG = "RemoteDataSource"
    }
}