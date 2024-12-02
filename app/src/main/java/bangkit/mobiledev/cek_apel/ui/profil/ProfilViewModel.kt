package bangkit.mobiledev.cek_apel.ui.profil

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bangkit.mobiledev.cek_apel.database.entity.UserProfile
import bangkit.mobiledev.cek_apel.database.repository.UserRepository
import bangkit.mobiledev.cek_apel.database.room.UserRoomDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfilViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository = UserRepository(
        UserRoomDatabase.getDatabase(application),
        FirebaseAuth.getInstance()
    )

    val userProfile: LiveData<UserProfile?> = repository.getCurrentUserProfile().asLiveData()

    val isLoggedIn: Boolean = repository.isUserLoggedIn()

    fun updateProfile(name: String, profileImageUri: String? = null) {
        viewModelScope.launch {
            repository.updateUserProfile(
                name = name,
                profileImageUri = profileImageUri?.let { Uri.parse(it) }
            )
        }
    }

    fun logout() {
        repository.logout()
    }

    fun getCurrentUserEmail(): String? = repository.getCurrentUserEmail()
}