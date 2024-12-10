package bangkit.mobiledev.cek_apel.forgot_password

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Send Reset Link
        binding.sendResetLinkButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()

            if (email.isNotEmpty()) {
                // Show ProgressBar
                binding.progressBar.visibility = android.view.View.VISIBLE

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        // Hide ProgressBar
                        binding.progressBar.visibility = android.view.View.GONE

                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Tautan reset kata sandi telah dikirim ke $email",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Gagal mengirim tautan: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Harap masukkan email Anda", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.hide()
    }
}