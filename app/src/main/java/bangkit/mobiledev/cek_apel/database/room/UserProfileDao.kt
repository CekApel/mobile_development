package bangkit.mobiledev.cek_apel.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import bangkit.mobiledev.cek_apel.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE email = :email")
    fun getUserProfile(email: String): Flow<UserProfile?>

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("DELETE FROM user_profile WHERE email = :email")
    suspend fun deleteProfile(email: String)
}