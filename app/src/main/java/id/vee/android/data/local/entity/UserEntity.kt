package id.vee.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("firstname")
    val firstName: String,

    @field:SerializedName("lastname")
    val lastName: String,

    @field:SerializedName("email")
    val email: String
)