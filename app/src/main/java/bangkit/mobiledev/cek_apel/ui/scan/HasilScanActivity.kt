package bangkit.mobiledev.cek_apel.ui.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.R
import bangkit.mobiledev.cek_apel.databinding.ActivityHasilScanBinding
import bangkit.mobiledev.cek_apel.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class HasilScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilScanBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    companion object {
        const val KEY_IMG_URI = "img_uri_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Log.e(this::class.java.simpleName, "Error: $error")
                        Toast.makeText(
                            this@HasilScanActivity,
                            "Analysis failed: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val highestCategory = it[0].categories.maxByOrNull { category -> category.score }
                                highestCategory?.let { category ->
                                    val label = imageClassifierHelper.labels[category.index]
                                    val displayResult = "$label: " +
                                            NumberFormat.getPercentInstance().format(category.score).trim()
                                    binding.resultText.text = displayResult
                                }
                            } else {
                                binding.resultText.text = getString(R.string.no_results_found)
                            }
                        }
                    }
                }
            }
        )

        intent.getStringExtra(KEY_IMG_URI)?.let { imageUriString ->
            val imageUri = Uri.parse(imageUriString)
            displayImage(imageUri)
            imageClassifierHelper.classifyStaticImage(imageUri)
        }
    }

    private fun displayImage(uri: Uri) {
        Log.d(this::class.java.simpleName, "Displaying image: $uri")
        binding.resultImage.setImageURI(uri)
    }
}
