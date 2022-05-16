package id.vee.android.data

import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.BasicResponse
import kotlinx.coroutines.flow.Flow

interface VeeDataSource {
    fun getUser(): Flow<UserEntity>
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse>
}