package id.vee.android.domain.usecase

import androidx.paging.PagingData
import id.vee.android.data.Resource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.ForecastResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.*
import id.vee.android.domain.repository.VeeDataSource
import kotlinx.coroutines.flow.Flow

class VeeInteractor(private val repository: VeeDataSource) : VeeUseCase {
    override fun getUser(): Flow<User?> = repository.getUser()

    override fun getToken(): Flow<Token?> = repository.getToken()

    override fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse> =
        repository.signup(firstName, lastName, email, password, passwordConfirm)

    override fun login(email: String, password: String): Flow<LoginResponse> =
        repository.login(email, password)

    override fun loginGoogle(token: String): Flow<LoginResponse> = repository.loginGoogle(token)

    override fun refreshToken(refreshToken: String): Flow<LoginResponse> =
        repository.refreshToken(refreshToken)

    override suspend fun deleteUser() = repository.deleteUser()

    override suspend fun deleteToken() = repository.deleteToken()

    override fun deleteTokenNetwork(token: String): Flow<BasicResponse> =
        repository.deleteTokenNetwork(token)

    override fun userDetail(data: Token): Flow<UserDetailResponse> = repository.userDetail(data)

    override suspend fun saveToken(data: Token) = repository.saveToken(data)

    override suspend fun saveUser(user: User) = repository.saveUser(user)

    override fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse> =
        repository.insertActivity(token, date, distance, litre, expense, lat, long)

    override fun updateName(
        token: String,
        firstName: String,
        lastName: String
    ): Flow<BasicResponse> =
        repository.updateName(token, firstName, lastName)

    override fun updatePassword(
        token: String,
        passwordCurrent: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse> =
        repository.updatePassword(token, passwordCurrent, password, passwordConfirm)

    override fun addPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): Flow<BasicResponse> =
        repository.addPassword(token, password, passwordConfirm)


    override fun getActivity(token: String, initMonthString: String?): Flow<Resource<List<Activity>>> =
        repository.getActivity(token, initMonthString)

    override fun getPagedActivity(token: String): Flow<PagingData<Activity>> = repository.getPagedActivity(token)

    override fun deleteActivity(accessToken: String, id: String): Flow<BasicResponse> =
        repository.deleteActivity(accessToken, id)

    override fun updateActivity(
        id: String,
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        long: Double
    ): Flow<BasicResponse> =
        repository.updateActivity(id, token, date, distance, litre, expense, lat, long)

    override fun getGasStations(
        token: String,
        lat: Double,
        lon: Double
    ): Flow<Resource<List<GasStations>>> =
        repository.getGasStations(token, lat, lon)

    override suspend fun getLocalStations(): Flow<List<GasStations>> = repository.getLocalStations()

    override suspend fun getRobo(month: String): Flow<Robo> = repository.getRobo(month)

    override suspend fun getNotification(): Flow<List<Notification>> = repository.getNotification()

    override fun getForecast(token: String): Flow<ForecastResponse> = repository.getForecast(token)
}