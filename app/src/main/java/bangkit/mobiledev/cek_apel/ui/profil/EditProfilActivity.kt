package bangkit.mobiledev.cek_apel.ui.profil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.ActivityEditProfilBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class EditProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var profilViewModel: ProfilViewModel
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.editProfileImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profilViewModel = ViewModelProvider(this)[ProfilViewModel::class.java]
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Pulihkan selectedImageUri jika ada
        if (savedInstanceState != null) {
            val uriString = savedInstanceState.getString("selectedImageUri")
            uriString?.let {
                selectedImageUri = Uri.parse(it)
                Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop()
                    .into(binding.editProfileImage)
            }
        }

        // Observe current profile
        profilViewModel.userProfile.observe(this) { profile ->
            profile?.let {
                binding.editName.setText(it.name)
                if (selectedImageUri == null) {  // Tampilkan gambar profil asli jika belum ada gambar baru
                    it.profileImageUri?.let { uri ->
                        Glide.with(this)
                            .load(Uri.parse(uri))
                            .circleCrop()
                            .into(binding.editProfileImage)
                    }
                }
            }
        }

        // Change photo button
        binding.changePhotoButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(galleryIntent)
        }

        // Save profile button
        binding.saveProfileButton.setOnClickListener {
            saveProfile()
        }

        supportActionBar?.hide()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let {
            outState.putString("selectedImageUri", it.toString())
        }
    }

    private fun saveProfile() {
        val name = binding.editName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            return
        }

        profilViewModel.updateProfile(
            name = name,
            profileImageUri = selectedImageUri?.toString()
        )

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}
