package id.vee.android.data.local

import id.vee.android.data.local.entity.ActivityEntity
import id.vee.android.data.local.entity.GasStationsEntity
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.local.room.VeeDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val mUserDao: VeeDao
) {
    fun getUser(): Flow<UserEntity?> = mUserDao.getUser()
    suspend fun deleteToken() {
        mUserDao.deleteToken()
    }

    suspend fun saveToken(data: TokenEntity) {
        mUserDao.saveToken(data)
    }

    suspend fun deleteUser() {
        mUserDao.deleteUser()
    }

    suspend fun saveUser(user: UserEntity) {
        mUserDao.insertUser(user)
    }

    fun getToken(): Flow<TokenEntity?> {
        return mUserDao.getToken()
    }

    suspend fun insertActivity(activityList: List<ActivityEntity>) =
        mUserDao.insertActivity(activityList)

    suspend fun insertGasStations(gasStationsList: List<GasStationsEntity>) =
        mUserDao.insertGasStations(gasStationsList)

    fun getActivity(): Flow<List<ActivityEntity>> = mUserDao.getActivity()

    fun getGasStations(): Flow<List<GasStationsEntity>> = mUserDao.getGasStations()

    fun getNearestGasStation(latitude: Double, longitude: Double): Flow<List<GasStationsEntity>> =
        mUserDao.getNearestGasStation(latitude, longitude)

    suspend fun deleteGasStations() = mUserDao.deleteGasStations()
    suspend fun deleteActivities() = mUserDao.deleteActivities()
}