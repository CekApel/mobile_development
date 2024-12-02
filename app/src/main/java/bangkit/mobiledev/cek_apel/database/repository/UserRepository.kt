package bangkit.mobiledev.cek_apel.database.repository

import android.net.Uri
import bangkit.mobiledev.cek_apel.database.entity.UserProfile
import bangkit.mobiledev.cek_apel.database.room.UserRoomDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val database: UserRoomDatabase,
    private val auth: FirebaseAuth
) {
    private val userProfileDao = database.userProfileDao()

    fun getCurrentUserProfile(): Flow<UserProfile?> {
        val currentUser = auth.currentUser
        return currentUser?.email?.let { email ->
            userProfileDao.getUserProfile(email)
        } ?: throw IllegalStateException("No logged-in user")
    }

    suspend fun updateUserProfile(
        name: String,
        profileImageUri: Uri? = null
    ) = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No logged-in user")

        val updatedProfile = UserProfile(
            email = currentUser.email!!,
            name = name,
            profileImageUri = profileImageUri?.toString()
        )

        userProfileDao.insertProfile(updatedProfile)
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserEmail(): String? = auth.currentUser?.email
}