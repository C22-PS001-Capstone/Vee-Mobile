package id.vee.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_entities")
data class NotificationEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "notification")
    val notification: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)