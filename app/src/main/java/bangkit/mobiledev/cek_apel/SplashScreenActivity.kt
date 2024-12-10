package bangkit.mobiledev.cek_apel

import android.annotation.SuppressLint
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

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            checkLoginStatus()
        }

        supportActionBar?.hide()
    }

    private fun checkLoginStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        } else {
            navigateToOnboardingActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
