package bangkit.mobiledev.cek_apel.ui.scan

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.ActivityHasilScanBinding
import bangkit.mobiledev.cek_apel.utils.uriToFile
import java.io.File

class HasilScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilScanBinding
    private val viewModel: HasilScanViewModel by viewModels()

    companion object {
        const val KEY_IMG_URI = "img_uri_key"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide action bar
        supportActionBar?.hide()

        // Set up back navigation
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }

        // Get image URI from intent
        val imageUriString = intent.getStringExtra(KEY_IMG_URI)
        if (imageUriString == null) {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imageUri = Uri.parse(imageUriString)
        val imageFile = uriToFile(imageUri, this)

        // Display the image
        binding.resultImage.setImageURI(imageUri)

        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        binding.resultContainer.visibility = View.GONE

        // Predict the image
        viewModel.predictImage(imageFile)

        // Observe prediction results
        viewModel.predictionResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            binding.resultContainer.visibility = View.VISIBLE

            result.onSuccess { mlResponse ->
                // Update UI with ML prediction results
                binding.resultTitle.text = mlResponse.data.result
                binding.confidenceText.text = "Confidence: ${mlResponse.data.confidenceScore}%"
                binding.resultDescription.text = mlResponse.data.explanation

                // Set suggestions
                val suggestions = when (val suggestion = mlResponse.data.suggestion) {
                    is String -> listOf(suggestion)
                    is List<*> -> suggestion.filterIsInstance<String>()
                    else -> emptyList()
                }
                binding.suggestionText.text = mlResponse.data.suggestion.toString()

                // Set medicine recommendations
                val medicines = when (val medicine = mlResponse.data.medicine) {
                    is String -> listOf(medicine)
                    is List<*> -> medicine.filterIsInstance<String>()
                    else -> emptyList()
                }
                binding.medicineText.text = mlResponse.data.medicine.toString()

                // Set created at timestamp
                binding.createdAt.text = "Created at: ${mlResponse.data.createdAt}"
            }

            result.onFailure { exception ->
                // Handle error scenario
                Toast.makeText(this, "Prediction failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.resultTitle.text = "Prediction Error"
                binding.resultDescription.text = exception.message ?: "Unknown error occurred"
            }
        }
    }
}
