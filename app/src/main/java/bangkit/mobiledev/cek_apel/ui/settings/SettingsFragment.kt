package bangkit.mobiledev.cek_apel.ui.settings

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import bangkit.mobiledev.cek_apel.databinding.FragmentSettingsBinding
import bangkit.mobiledev.cek_apel.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var settingsPreferences: SettingsPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Initialize ViewModel and Settings Manager
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsPreferences = SettingsPreferences(requireContext())

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
        setupButtonListeners()

        // Setup notification toggle
        setupNotificationToggle()

        return binding.root
    }

    private fun setupButtonListeners() {
        binding.editProfileChevron.setOnClickListener {
            val intent = Intent(requireContext(), EditProfilActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            settingsViewModel.logout()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        binding.languageChevron.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.aboutChevron.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, ensure notification is enabled
            binding.notificationSwitch.isChecked = true
            DailyReminderWorker.scheduleReminder(requireContext())
        } else {
            // Permission denied, uncheck the switch
            binding.notificationSwitch.isChecked = false
        }
    }

    private fun setupNotificationToggle() {
        // Observe current notification setting
        viewLifecycleOwner.lifecycleScope.launch {
            settingsPreferences.isNotificationEnabled.collect { isEnabled ->
                val hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                // Update switch state based on both saved preference and permission
                binding.notificationSwitch.isChecked = isEnabled && hasPermission
            }
        }

        // Set listener for switch
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Check and request notification permission for Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // Permission already granted
                            scheduleNotification(true)
                        }
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            // Show rationale dialog
                            showPermissionRationaleDialog()
                        }
                        else -> {
                            // Request permission
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    // For older Android versions, just schedule
                    scheduleNotification(true)
                }
            } else {
                // Cancel notification when switch is turned off
                scheduleNotification(false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun scheduleNotification(isEnabled: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isEnabled && (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || hasNotificationPermission())) {
                settingsPreferences.setNotificationEnabled(true)
                DailyReminderWorker.scheduleReminder(requireContext())
            } else {
                settingsPreferences.setNotificationEnabled(false)
                DailyReminderWorker.cancelReminder(requireContext())
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Notification Permission")
            .setMessage("This app needs notification permission to send daily reminders. Would you like to grant permission?")
            .setPositiveButton("Yes") { _, _ ->
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("No") { dialog, _ ->
                binding.notificationSwitch.isChecked = false
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}