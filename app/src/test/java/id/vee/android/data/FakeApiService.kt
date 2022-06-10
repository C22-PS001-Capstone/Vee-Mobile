package id.vee.android.data

import id.vee.android.DataDummy
import id.vee.android.data.remote.network.ApiService
import id.vee.android.data.remote.response.*

class FakeApiService : ApiService {
    private val dummy = DataDummy
    override suspend fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse =
        dummy.getBasicResponse()


    override suspend fun updateName(
        token: String,
        firstName: String,
        lastName: String
    ): BasicResponse =
        dummy.getBasicResponse()

    override suspend fun updatePassword(
        token: String,
        passwordCurrent: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse =
        dummy.getBasicResponse()

    override suspend fun addPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): BasicResponse =
        dummy.getBasicResponse()

    override suspend fun loginGoogle(token: String): LoginResponse =
        dummy.getLoginResponse()

    override suspend fun login(email: String, password: String): LoginResponse =
        dummy.getLoginResponse()

    override suspend fun userDetail(token: String): UserDetailResponse = dummy.getUserDetail()

    override suspend fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        lon: Double
    ): BasicResponse = dummy.getBasicResponse()

    override suspend fun refreshToken(refreshToken: String): LoginResponse =
        dummy.getLoginResponse()

    override suspend fun logout(refreshToken: String): BasicResponse =
        dummy.getBasicResponse()

    override suspend fun getActivity(token: String): ActivityListResponse =
        dummy.getActivityListResponse()

    override suspend fun getPaggedActivity(
        token: String,
        page: Int,
        pageSize: Int
    ): ActivityListResponse = dummy.getActivityListResponse()

    override suspend fun deleteActivity(id: String, bearer: String): BasicResponse =
        dummy.getBasicResponse()

    override suspend fun updateActivity(
        token: String,
        id: String,
        date: String,
        distance: Int,
        litre: Double,
        expense: Int,
        lat: Double,
        lon: Double
    ): BasicResponse = dummy.getBasicResponse()

    override suspend fun getGasStations(
        token: String,
        lat: Double,
        lon: Double
    ): GasStationsListResponse = dummy.getGasStationsListResponse()

    override suspend fun getForecast(token: String): ForecastResponse = dummy.getForecastResponse()
}