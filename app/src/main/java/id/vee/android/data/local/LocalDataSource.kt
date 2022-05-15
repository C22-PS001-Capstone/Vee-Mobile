package id.vee.android.data.local

import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val mUserDao: UserDao
) {
    fun getUser(): Flow<UserEntity> = mUserDao.getUser()

    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(mUserDao: UserDao): LocalDataSource =
            instance ?: LocalDataSource(mUserDao).apply {
                instance = this
            }
    }
}