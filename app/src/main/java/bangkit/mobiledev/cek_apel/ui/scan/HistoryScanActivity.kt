package bangkit.mobiledev.cek_apel.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.cek_apel.databinding.ActivityHistoryScanBinding

class HistoryScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryScanBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)


    }

}
