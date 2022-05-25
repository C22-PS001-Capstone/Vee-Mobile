package id.vee.android.data.local

import id.vee.android.data.local.entity.ActivityEntity
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

    fun getActivity(): Flow<List<ActivityEntity>> = mUserDao.getActivity()
    suspend fun deleteActivities() = mUserDao.deleteActivities()
    suspend fun deleteActivity(id: String) = mUserDao.deleteActivity(id)
}