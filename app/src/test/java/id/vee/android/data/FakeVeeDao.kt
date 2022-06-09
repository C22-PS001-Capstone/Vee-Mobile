package id.vee.android.data

import androidx.paging.PagingSource
import id.vee.android.DataDummy
import id.vee.android.data.local.entity.*
import id.vee.android.data.local.room.VeeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeVeeDao : VeeDao {
    private val dummy = DataDummy
    private var userData = mutableListOf<UserEntity>()
    private var tokenData = mutableListOf<TokenEntity>()
    private var activityData = mutableListOf<ActivityEntity>()
    private var gasStationsData = mutableListOf<GasStationsEntity>()
    private var notificationData = mutableListOf<NotificationEntity>()
    private val roboData = dummy.getRoboEntity()

    override suspend fun deleteUser() {
        userData.clear()
    }

    override fun getUser(): Flow<UserEntity?> {
        return userData.asFlow().map { it }
    }

    override suspend fun insertUser(user: UserEntity) {
        userData.add(user)
    }

    override suspend fun saveToken(data: TokenEntity) {
        tokenData.add(data)
    }

    override suspend fun deleteToken() {
        tokenData.clear()
    }

    override fun getToken(): Flow<TokenEntity?> {
        return tokenData.asFlow().map { it }
    }

    override suspend fun insertActivity(activityList: List<ActivityEntity>) {
        activityData.addAll(activityList)
    }

    override fun getActivity(): Flow<List<ActivityEntity>> {
        return flowOf(activityData)
    }

    override fun getPagedActivity(): PagingSource<Int, ActivityEntity> {
        TODO("Not yet implemented")
    }

    override fun getActivityMonth(month: String): Flow<List<ActivityEntity>> {
        return flowOf(activityData)
    }

    override fun getRobo(month: String): Flow<RoboEntity> {
        return flowOf(roboData)
    }

    override suspend fun insertGasStations(gasStationsList: List<GasStationsEntity>) {
        gasStationsData.addAll(gasStationsList)
    }

    override fun getGasStations(): Flow<List<GasStationsEntity>> {
        return flowOf(gasStationsData)
    }

    override fun getNearestGasStation(
        latitude: Double,
        longitude: Double
    ): Flow<List<GasStationsEntity>> {
        return flowOf(gasStationsData)
    }

    override suspend fun deleteGasStations(ids: List<String>) {
        gasStationsData.removeAll {
            ids.contains(it.id)
        }
    }

    override suspend fun deleteActivities() {
        activityData.clear()
    }

    override suspend fun insertNotification(notification: NotificationEntity) {
        notificationData.add(notification)
    }

    override suspend fun deleteNotification() {
        notificationData.clear()
    }

    override fun getNotification(): Flow<List<NotificationEntity>> {
        return flowOf(notificationData)
    }
}