package bangkit.mobiledev.cek_apel.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        // Initialize view binding
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup back navigation for the toolbar
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Setup FAQ interactions
        setupFAQInteractions()
    }

    private fun setupFAQInteractions() {
        // FAQ 1 interaction
        binding.faqQuestion1.setOnClickListener {
            toggleFAQAnswer(binding.faqAnswer1)
        }

        // FAQ 2 interaction
        binding.faqQuestion2.setOnClickListener {
            toggleFAQAnswer(binding.faqAnswer2)
        }
    }

    private fun toggleFAQAnswer(answerView: android.widget.TextView) {
        // Toggle visibility of the answer
        answerView.visibility = if (answerView.visibility == android.view.View.VISIBLE) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
}