package bangkit.mobiledev.cek_apel.ui.settings

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import bangkit.mobiledev.cek_apel.R
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

        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsPreferences = SettingsPreferences(requireContext())

        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.userEmail.text = currentUser?.email ?: "No email"

        settingsViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.userName.text = profile?.name ?: "User Name"

            profile?.profileImageUri?.let { uriString ->
                Glide.with(requireContext())
                    .load(Uri.parse(uriString))
                    .circleCrop()
                    .into(binding.profileImage)
            }
        }

        setupButtonListeners()

        setupNotificationToggle()

        return binding.root
    }

    private fun setupButtonListeners() {
        binding.editProfileChevron.setOnClickListener {
            val intent = Intent(requireContext(), EditProfilActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.aboutChevron.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirmation_title))
            .setMessage(getString(R.string.logout_confirmation_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                settingsViewModel.logout()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            binding.notificationSwitch.isChecked = true
            DailyReminderWorker.scheduleReminder(requireContext())
        } else {
            binding.notificationSwitch.isChecked = false
        }
    }

    private fun setupNotificationToggle() {
        viewLifecycleOwner.lifecycleScope.launch {
            settingsPreferences.isNotificationEnabled.collect { isEnabled ->
                val hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                binding.notificationSwitch.isChecked = isEnabled && hasPermission
            }
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            scheduleNotification(true)
                        }
                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            showPermissionRationaleDialog()
                        }
                        else -> {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    scheduleNotification(true)
                }
            } else {
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