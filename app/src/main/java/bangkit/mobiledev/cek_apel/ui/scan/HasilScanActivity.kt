package bangkit.mobiledev.cek_apel.ui.scan

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityHasilScanBinding
import bangkit.mobiledev.cek_apel.utils.uriToFile

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

        supportActionBar?.hide()

        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }

        val imageUriString = intent.getStringExtra(KEY_IMG_URI)
        if (imageUriString == null) {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imageUri = Uri.parse(imageUriString)
        val imageFile = uriToFile(imageUri, this)

        binding.resultImage.setImageURI(imageUri)

        binding.progressBar.visibility = View.VISIBLE
        binding.resultContainer.visibility = View.GONE

        viewModel.predictImage(imageFile)

        viewModel.predictionResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            binding.resultContainer.visibility = View.VISIBLE

            result.onSuccess { mlResponse ->
                binding.resultTitle.text = mlResponse.data.result
                binding.confidenceText.text = "Confidence: ${mlResponse.data.confidenceScore}%"
                binding.resultDescription.text = mlResponse.data.explanation

                binding.suggestionText.text = mlResponse.data.suggestion

                binding.medicineText.text = mlResponse.data.medicine

                binding.createdAt.text = "Dibuat pada: ${mlResponse.data.createdAt}"
            }

            result.onFailure { exception ->
                Toast.makeText(this, "Prediction failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.resultTitle.text = "Prediction Error"
                binding.resultDescription.text = exception.message ?: "Unknown error occurred"
            }
        }
    }
}
