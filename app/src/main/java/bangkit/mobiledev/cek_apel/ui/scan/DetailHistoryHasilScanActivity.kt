package bangkit.mobiledev.cek_apel.ui.scan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailHistoryHasilScanBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DetailHistoryHasilScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryHasilScanBinding
    private val detailViewModel: DetailHistoryViewModel by viewModels()

    companion object {
        const val EXTRA_SCAN_HISTORY_ID = "extra_scan_history_id"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }

        val extraScanHistoryId = intent.getIntExtra("extra_scan_history_id", -1).toLong()

        detailViewModel.loadScanHistoryById(extraScanHistoryId)
        detailViewModel.scanHistoryItem.observe(this) { scanHistory ->
            scanHistory?.let {
                binding.historyTitle.text = scanHistory.result
                binding.confidenceText.text = "Confidence: ${scanHistory.confidenceScore}%"
                binding.historyDescription.text = scanHistory.explanation
                binding.suggestionText.text = scanHistory.suggestion
                binding.medicineText.text = scanHistory.medicine

                val inputDateFormat = SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()
                )
                val outputDateFormat = SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()
                )

                val date = inputDateFormat.parse(scanHistory.scanDate)
                binding.createdAt.text =
                    "Dibuat pada: ${outputDateFormat.format(date)}"


                Glide.with(this)
                    .load(scanHistory.imageUri)
                    .into(binding.historyImage)
            }
        }
    }
}