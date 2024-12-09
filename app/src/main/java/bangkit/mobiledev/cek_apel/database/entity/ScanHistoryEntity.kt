package bangkit.mobiledev.cek_apel.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String,
    val result: String,
    val confidenceScore: Int,
    val scanDate: String,
    val explanation: String,
    val suggestion: String,
    val medicine: String
)