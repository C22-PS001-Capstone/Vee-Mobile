package id.vee.android.domain.usecase

import androidx.paging.PagingData
import id.vee.android.data.Resource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.ForecastResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.*
import kotlinx.coroutines.flow.Flow

interface VeeUseCase {
    fun getUser(): Flow<User?>

    fun getToken(): Flow<Token?>

    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse>

    fun login(email: String, password: String): Flow<LoginResponse>

    fun refreshToken(refreshToken: String): Flow<LoginResponse>

    suspend fun deleteUser()

    suspend fun deleteToken()

    fun deleteTokenNetwork(token: String): Flow<BasicResponse>

    fun userDetail(data: Token): Flow<UserDetailResponse>

    suspend fun saveToken(data: Token)

    suspend fun saveUser(user: User)

    fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse>

    fun updateName(
        token: String,
        firstName: String,
        lastName: String
    ): Flow<BasicResponse>

    fun updatePassword(
        token: String,
        passwordCurrent: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse>

    fun addPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse>

    fun getGasStations(
        token: String,
        lat: Double,
        lon: Double
    ): Flow<Resource<List<GasStations>>>

    fun getActivity(token: String, initMonthString: String? = null): Flow<Resource<List<Activity>>>
    fun getPagedActivity(token: String): Flow<PagingData<Activity>>

    fun deleteActivity(accessToken: String, id: String): Flow<BasicResponse>
    fun updateActivity(
        id: String,
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse>

    fun loginGoogle(token: String): Flow<LoginResponse>
    suspend fun getLocalStations(): Flow<List<GasStations>>

    suspend fun getRobo(month: String): Flow<Robo>
    suspend fun getNotification(): Flow<List<Notification>>

    fun getForecast(token: String): Flow<ForecastResponse>
}