package id.vee.android.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VeeDao {
    @Query("DELETE FROM user WHERE 1")
    suspend fun deleteUser()

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(data: TokenEntity)

    @Query("DELETE FROM token WHERE 1")
    suspend fun deleteToken()
}