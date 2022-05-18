package id.vee.android.data

import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import kotlinx.coroutines.flow.Flow

interface VeeDataSource {
    fun getUser(): Flow<UserEntity>
    fun getToken(): Flow<TokenEntity>
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse>

    fun login(email: String, password: String): Flow<LoginResponse>
    fun userDetail(data: TokenEntity): Flow<UserDetailResponse>
    suspend fun saveToken(data: TokenEntity)
    suspend fun saveUser(user: UserEntity)
    fun insertActivity(token: String, date: String, distance: Int, litre: Int, expense: Int): Flow<BasicResponse>
    fun refreshToken(refreshToken: String): Flow<LoginResponse>
}