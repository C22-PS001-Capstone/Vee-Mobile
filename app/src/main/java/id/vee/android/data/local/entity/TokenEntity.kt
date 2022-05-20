package id.vee.android.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "token", indices = [Index(value = ["accessToken"], unique = true)])
data class TokenEntity(
    @PrimaryKey
    var accessToken: String,
    var refreshToken: String,
    @SerializedName("maxTokenAgeSec")
    var expiredAt: Long,
)