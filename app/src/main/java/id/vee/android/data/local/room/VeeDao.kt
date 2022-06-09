package id.vee.android.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.vee.android.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VeeDao {
    @Query("DELETE FROM user WHERE 1")
    suspend fun deleteUser()

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(data: TokenEntity)

    @Query("DELETE FROM token WHERE 1")
    suspend fun deleteToken()

    @Query("SELECT * FROM token LIMIT 1")
    fun getToken(): Flow<TokenEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activityList: List<ActivityEntity>)

    @Query("SELECT * FROM activity_entities")
    fun getActivity(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activity_entities")
    fun getPagedActivity(): PagingSource<Int, ActivityEntity>

    @Query("SELECT * FROM activity_entities WHERE date LIKE :month")
    fun getActivityMonth(month: String): Flow<List<ActivityEntity>>

    @Query("SELECT SUM(liter) as ltr, SUM(price) as prc FROM activity_entities WHERE date LIKE :month")
    fun getRobo(month: String): Flow<RoboEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasStations(gasStationsList: List<GasStationsEntity>)

    @Query("SELECT * FROM gas_stations_entities")
    fun getGasStations(): Flow<List<GasStationsEntity>>

    @Query("SELECT * FROM gas_stations_entities ORDER BY ABS(lat - :latitude) + ABS(lon - :longitude) ASC")
    fun getNearestGasStation(latitude: Double, longitude: Double): Flow<List<GasStationsEntity>>

    @Query("DELETE FROM gas_stations_entities WHERE id NOT IN (:ids)")
    suspend fun deleteGasStations(ids: List<String>)

    @Query("DELETE FROM activity_entities WHERE 1")
    suspend fun deleteActivities()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("DELETE FROM notification_entities WHERE 1")
    suspend fun deleteNotification()

    @Query("SELECT * FROM notification_entities")
    fun getNotification(): Flow<List<NotificationEntity>>
}