package id.vee.android.data.remote

import id.vee.android.data.remote.network.ApiService
import id.vee.android.data.remote.response.BasicResponse

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
            BasicResponse(
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
    }
}