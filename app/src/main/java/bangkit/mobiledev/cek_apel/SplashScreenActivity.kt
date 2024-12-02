package bangkit.mobiledev.cek_apel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivitySplashScreenBinding
import bangkit.mobiledev.cek_apel.ui.onboarding.OnboardingActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Tampilkan splash screen selama beberapa detik sebelum cek login
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000) // Splash Screen tampil selama 3 detik
            checkLoginStatus()
        }

        supportActionBar?.hide()
    }

    private fun checkLoginStatus() {
        // Periksa status autentikasi pengguna
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Pengguna sudah login, arahkan ke MainActivity
            navigateToMainActivity()
        } else {
            // Pengguna belum login, arahkan ke OnboardingActivity
            navigateToOnboardingActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Menutup SplashScreenActivity agar tidak bisa kembali
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish() // Menutup SplashScreenActivity agar tidak bisa kembali
    }
}
