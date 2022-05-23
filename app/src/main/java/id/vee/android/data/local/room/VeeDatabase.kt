package id.vee.android.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.vee.android.data.local.entity.ActivityEntity
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, TokenEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = true
)
abstract class VeeDatabase : RoomDatabase() {
    abstract fun veeDao(): VeeDao
}