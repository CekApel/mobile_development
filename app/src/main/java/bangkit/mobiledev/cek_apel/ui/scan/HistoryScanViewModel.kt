package bangkit.mobiledev.cek_apel.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity
import bangkit.mobiledev.cek_apel.database.repository.ScanHistoryRepository
import bangkit.mobiledev.cek_apel.database.room.ScanHistoryDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HistoryScanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ScanHistoryRepository
    val allScanHistory: LiveData<List<ScanHistoryEntity>>

    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user"
        val scanHistoryDao = ScanHistoryDatabase.getDatabase(application, userId).scanHistoryDao()
        repository = ScanHistoryRepository(scanHistoryDao)
        allScanHistory = repository.allScanHistory
    }

    fun insertScanHistory(scanHistory: ScanHistoryEntity) = viewModelScope.launch {
        repository.insertScanHistory(scanHistory)
    }

    fun deleteScanHistory(scanHistory: ScanHistoryEntity) = viewModelScope.launch {
        repository.deleteScanHistory(scanHistory)
    }

    fun deleteAllScanHistory() = viewModelScope.launch {
        repository.deleteAllScanHistory()
    }
}