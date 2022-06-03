package id.vee.android.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.vee.android.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        TokenEntity::class,
        ActivityEntity::class,
        GasStationsEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VeeDatabase : RoomDatabase() {
    abstract fun veeDao(): VeeDao
}