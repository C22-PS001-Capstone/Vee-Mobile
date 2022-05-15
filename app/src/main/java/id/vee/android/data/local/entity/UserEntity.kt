package id.vee.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("first_name")
    val firstName: String,

    @field:SerializedName("last_name")
    val lastName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("isLogin")
    var isLogin: Boolean = false
)