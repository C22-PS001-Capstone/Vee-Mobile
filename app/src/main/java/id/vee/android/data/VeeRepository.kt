package id.vee.android.data

import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.response.BasicResponse
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