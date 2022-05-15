package id.vee.android.data.local

import id.vee.android.data.local.room.UserDao

class LocalDataSource(
    private val mUserDao: UserDao
) {
    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance(mUserDao: UserDao): LocalDataSource =
            instance ?: LocalDataSource(mUserDao).apply {
                instance = this
            }
    }
}