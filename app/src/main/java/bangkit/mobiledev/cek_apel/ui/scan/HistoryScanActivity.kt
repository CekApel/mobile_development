package bangkit.mobiledev.cek_apel.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.mobiledev.cek_apel.adapter.ScanHistoryAdapter
import bangkit.mobiledev.cek_apel.databinding.ActivityHistoryScanBinding

class HistoryScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryScanBinding
    private val viewModel: HistoryScanViewModel by viewModels()
    private lateinit var scanHistoryAdapter: ScanHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupRecyclerView()
        observeScanHistory()
        setupBackNavigation()
    }

    private fun setupRecyclerView() {
        scanHistoryAdapter = ScanHistoryAdapter(
            onDeleteClick = { scanHistory ->
                viewModel.deleteScanHistory(scanHistory)
            },
            onDetailClick = { scanHistory ->
                val intent = Intent(this, DetailHistoryHasilScanActivity::class.java).apply {
                    putExtra(DetailHistoryHasilScanActivity.EXTRA_SCAN_HISTORY_ID, scanHistory.id)
                }
                startActivity(intent)
            }
        )

        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryScanActivity)
            adapter = scanHistoryAdapter
        }
    }

    private fun observeScanHistory() {
        viewModel.allScanHistory.observe(this) { scanHistoryList ->
            scanHistoryAdapter.submitList(scanHistoryList)

            binding.emptyStateLayout.visibility =
                if (scanHistoryList.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupBackNavigation() {
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}