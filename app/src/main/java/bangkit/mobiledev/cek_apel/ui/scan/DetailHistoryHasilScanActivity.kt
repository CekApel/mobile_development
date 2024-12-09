package bangkit.mobiledev.cek_apel.ui.scan

import android.net.Uri
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide ActionBar
        supportActionBar?.hide()

        // Setup back navigation
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }


    }
}