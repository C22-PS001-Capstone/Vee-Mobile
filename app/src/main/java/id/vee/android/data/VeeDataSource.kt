package id.vee.android.data

import id.vee.android.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface VeeDataSource {
    fun getUser(): Flow<UserEntity>
}