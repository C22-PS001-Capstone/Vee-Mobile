package id.vee.android.data.remote

import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.remote.network.ApiResponse
import id.vee.android.data.remote.network.ApiService
import id.vee.android.data.remote.response.*
import id.vee.android.utils.bearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

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

    suspend fun loginGoogle(token: String): LoginResponse {
        return try {
            apiService.loginGoogle(token.bearer())
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
        litre: Double,
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

    suspend fun addPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse {
        return try {
            apiService.addPassword(token.bearer(), password, passwordConfirm)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }
    }

    suspend fun getGasStations(
        token: String,
        lat: Double,
        lon: Double
    ): Flow<ApiResponse<List<GasStationsResponse>>> = flow {
        try {
            val response = apiService.getGasStations(token.bearer(), lat, lon)
            emit(ApiResponse.Success(response.data))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getActivity(token: String): Flow<ApiResponse<List<ActivityResponse>>> = flow {
        try {
            val response = apiService.getActivity(token.bearer())
            emit(ApiResponse.Success(response.data.activities))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPagedActivity(token: String, page: Int, pageSize: Int): List<ActivityResponse> {
        val response = apiService.getPaggedActivity(token.bearer(), page, pageSize)
        Timber.d("Response getPagedActivity: ${response.data.activities}")
        return response.data.activities
    }

    suspend fun deleteActivity(accessToken: String, id: String): BasicResponse =
        try {
            apiService.deleteActivity(id, accessToken.bearer())
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }

    suspend fun updateActivity(
        id: String,
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        long: Double
    ): BasicResponse =
        try {
            apiService.updateActivity(token.bearer(), id, date, distance, litre, expense, lat, long)
        } catch (e: Exception) {
            BasicResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }

    suspend fun getForecast(token: String): ForecastResponse =
        try {
            apiService.getForecast(token.bearer())
        } catch (e: Exception) {
            ForecastResponse(
                status = "error",
                message = e.message.toString(),
                data = null
            )
        }

}