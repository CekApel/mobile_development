package bangkit.mobiledev.cek_apel.ui.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bangkit.mobiledev.cek_apel.databinding.ActivityDetailHistoryHasilScanBinding

class DetailHistoryHasilScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryHasilScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}