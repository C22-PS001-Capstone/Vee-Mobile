package id.vee.android.data.local

import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.local.room.VeeDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val mUserDao: VeeDao
) {
    fun getUser(): Flow<UserEntity> = mUserDao.getUser()
    suspend fun deleteToken() {
        mUserDao.deleteToken()
    }

    suspend fun saveToken(data: TokenEntity) {
        mUserDao.saveToken(data)
    }

    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(mUserDao: VeeDao): LocalDataSource =
            instance ?: LocalDataSource(mUserDao).apply {
                instance = this
            }
    }
}