package bangkit.mobiledev.cek_apel.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity

@Dao
interface ScanHistoryDao {
    @Query("SELECT * FROM scan_history ORDER BY id DESC")
    fun getAllScanHistory(): LiveData<List<ScanHistoryEntity>>

    @Insert
    suspend fun insertScanHistory(scanHistory: ScanHistoryEntity)

    @Delete
    suspend fun deleteScanHistory(scanHistory: ScanHistoryEntity)

    @Query("DELETE FROM scan_history")
    suspend fun deleteAllScanHistory()
}