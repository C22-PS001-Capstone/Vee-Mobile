package id.vee.android.data

import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VeeRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : VeeDataSource {
    override fun getUser(): Flow<UserEntity> {
        return localDataSource.getUser()
    }

    override fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse> = flow {
        emit(
            remoteDataSource.signup(
                firstName,
                lastName,
                email,
                password,
                passwordConfirm
            )
        )
    }

    override fun login(email: String, password: String): Flow<LoginResponse> {
        return flow {
            emit(
                remoteDataSource.login(
                    email,
                    password
                )
            )
        }
    }

    override fun userDetail(data: TokenEntity): Flow<UserDetailResponse> {
        return flow{
            emit(
                remoteDataSource.userDetail(data)
            )
        }
    }

    override suspend fun saveToken(data: TokenEntity) {
        localDataSource.deleteToken()
        localDataSource.saveToken(data)
    }

    override suspend fun saveUser(user: UserEntity) {
        localDataSource.deleteUser()
        localDataSource.saveUser(user)
    }

    companion object {
        @Volatile
        private var instance: VeeRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            appExecutors: AppExecutors
        ): VeeRepository =
            instance ?: synchronized(this) {
                VeeRepository(remoteDataSource, localDataSource, appExecutors).apply {
                    instance = this
                }
            }
    }
}