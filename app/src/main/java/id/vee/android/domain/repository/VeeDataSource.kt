package id.vee.android.domain.repository

import id.vee.android.data.Resource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.*
import kotlinx.coroutines.flow.Flow

interface VeeDataSource {
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
    fun loginGoogle(token: String): Flow<LoginResponse>
    fun userDetail(data: Token): Flow<UserDetailResponse>
    suspend fun saveToken(data: Token)
    suspend fun saveUser(user: User)
    fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Int,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse>

    fun refreshToken(refreshToken: String): Flow<LoginResponse>
    suspend fun deleteUser()
    suspend fun deleteToken()
    fun deleteTokenNetwork(token: String): Flow<BasicResponse>

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

    fun getActivity(token: String): Flow<Resource<List<Activity>>>

    fun deleteActivity(accessToken: String, id: String): Flow<BasicResponse>
    fun updateActivity(
        id: String,
        token: String,
        date: String,
        distance: Int,
        litre: Int,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse>

    suspend fun getLocalStations(): Flow<List<GasStations>>

    suspend fun getRobo(month: String): Flow<Robo>
    suspend fun insertNotification(notification: Notification)
    suspend fun getNotification(): Flow<List<Notification>>
}