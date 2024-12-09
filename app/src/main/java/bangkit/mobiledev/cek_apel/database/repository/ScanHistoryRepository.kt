package bangkit.mobiledev.cek_apel.database.repository

import androidx.lifecycle.LiveData
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity
import bangkit.mobiledev.cek_apel.database.room.ScanHistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScanHistoryRepository(private val scanHistoryDao: ScanHistoryDao) {
    val allScanHistory: LiveData<List<ScanHistoryEntity>> = scanHistoryDao.getAllScanHistory()

    suspend fun insertScanHistory(scanHistory: ScanHistoryEntity) {
        withContext(Dispatchers.IO) {
            scanHistoryDao.insertScanHistory(scanHistory)
        }
    }

    suspend fun deleteScanHistory(scanHistory: ScanHistoryEntity) {
        withContext(Dispatchers.IO) {
            scanHistoryDao.deleteScanHistory(scanHistory)
        }
    }

    suspend fun deleteAllScanHistory() {
        withContext(Dispatchers.IO) {
            scanHistoryDao.deleteAllScanHistory()
        }
    }
}