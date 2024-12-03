package bangkit.mobiledev.cek_apel.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bangkit.mobiledev.cek_apel.databinding.FragmentSettingsBinding
import bangkit.mobiledev.cek_apel.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        // Set email from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.userEmail.text = currentUser?.email ?: "No email"

        // Observe user profile
        settingsViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.userName.text = profile?.name ?: "User Name"

            profile?.profileImageUri?.let { uriString ->
                Glide.with(requireContext())
                    .load(Uri.parse(uriString))
                    .circleCrop()
                    .into(binding.profileImage)
            }
        }

        // Setup buttons
        binding.editProfileButton.setOnClickListener {
            startActivity(Intent(activity, EditProfilActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            settingsViewModel.logout()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
