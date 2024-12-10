package bangkit.mobiledev.cek_apel.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityOnboardingBinding
import bangkit.mobiledev.cek_apel.login.LoginActivity
import bangkit.mobiledev.cek_apel.MainActivity
import com.google.firebase.auth.FirebaseAuth

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMainActivity()
        } else {
            binding.button.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        supportActionBar?.hide()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}