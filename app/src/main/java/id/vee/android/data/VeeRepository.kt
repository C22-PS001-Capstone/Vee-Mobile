package id.vee.android.data

import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class VeeRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : VeeDataSource {
    override fun getUser(): Flow<User?> {
        return localDataSource.getUser().map {
            it?.let { user -> DataMapper.mapEntityToDomain(user) }
        }
    }

    override fun getToken(): Flow<Token?> {
        return localDataSource.getToken().mapNotNull {
            it?.let { DataMapper.mapEntityToDomain(it) }
        }
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

    override fun refreshToken(refreshToken: String): Flow<LoginResponse> {
        return flow {
            emit(
                remoteDataSource.refreshToken(refreshToken)
            )
        }
    }

    override suspend fun deleteUser() {
        return localDataSource.deleteUser()
    }

    override suspend fun deleteToken() {
        return localDataSource.deleteToken()
    }

    override fun deleteTokenNetwork(token: String): Flow<BasicResponse> {
        return flow {
            emit(
                remoteDataSource.logout(
                    token
                )
            )
        }
    }

    override fun userDetail(data: Token): Flow<UserDetailResponse> {
        return flow {
            emit(
                remoteDataSource.userDetail(DataMapper.mapDomainToEntity(data))
            )
        }
    }

    override suspend fun saveToken(data: Token) {
        localDataSource.deleteToken()
        localDataSource.saveToken(DataMapper.mapDomainToEntity(data))
    }

    override suspend fun saveUser(user: User) {
        localDataSource.deleteUser()
        localDataSource.saveUser(DataMapper.mapDomainToEntity(user))
    }

    override fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Int,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse> {
        return flow {
            emit(
                remoteDataSource.insertActivity(
                    token,
                    date,
                    distance,
                    litre,
                    expense,
                    lat,
                    long
                )
            )
        }
    }

    companion object {
        @Volatile
        private var instance: VeeRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
        ): VeeRepository =
            instance ?: synchronized(this) {
                VeeRepository(remoteDataSource, localDataSource).apply {
                    instance = this
                }
            }

        private const val TAG = "VeeRepository"
    }

}