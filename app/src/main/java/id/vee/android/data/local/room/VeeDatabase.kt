package id.vee.android.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, TokenEntity::class], version = 1, exportSchema = true)
abstract class VeeDatabase : RoomDatabase() {
    abstract fun veeDao(): VeeDao

    companion object {
        @Volatile
        private var instance: VeeDatabase? = null
        fun getInstance(context: Context): VeeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    VeeDatabase::class.java, "Vee.db"
                ).build()
            }
    }
}