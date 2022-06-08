package id.vee.android.data.local

import androidx.paging.PagingSource
import id.vee.android.data.local.entity.*
import id.vee.android.data.local.room.RemoteKeysDao
import id.vee.android.data.local.room.VeeDao
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class LocalDataSource(
    private val mUserDao: VeeDao,
    private val remoteKeysDao: RemoteKeysDao
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

    suspend fun insertGasStations(gasStationsList: List<GasStationsEntity>) {
        val ids: List<String> = gasStationsList.map { it.id }
        mUserDao.deleteGasStations(ids)
        Timber.d("ids: $ids")
        mUserDao.insertGasStations(gasStationsList)
    }

    fun getActivity(initMonthString: String?): Flow<List<ActivityEntity>> =
        if (initMonthString != null) {
            mUserDao.getActivityMonth("%$initMonthString%")
        } else {
            mUserDao.getActivity()
        }
    fun getPagedActivity(): PagingSource<Int, ActivityEntity> = mUserDao.getPagedActivity()

    fun getRobo(month: String): Flow<RoboEntity> = mUserDao.getRobo(month)

    fun getGasStations(): Flow<List<GasStationsEntity>> = mUserDao.getGasStations()

    fun getNearestGasStation(latitude: Double, longitude: Double): Flow<List<GasStationsEntity>> =
        mUserDao.getNearestGasStation(latitude, longitude)

    suspend fun deleteActivities() = mUserDao.deleteActivities()
    suspend fun insertNotification(mapDomainToEntity: NotificationEntity) =
        mUserDao.insertNotification(mapDomainToEntity)

    fun getNotification(): Flow<List<NotificationEntity>> = mUserDao.getNotification()

    suspend fun getRemoteKeysId(id: String) = remoteKeysDao.getRemoteKeysId(id)
    suspend fun insertKeys(keys: List<RemoteKeysEntity>) = remoteKeysDao.insertAll(keys)
    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()

}