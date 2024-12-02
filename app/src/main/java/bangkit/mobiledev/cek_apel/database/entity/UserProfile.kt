package bangkit.mobiledev.cek_apel.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val name: String,
    val profileImageUri: String? = null
)
