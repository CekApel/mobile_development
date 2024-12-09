package bangkit.mobiledev.cek_apel.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity

@Database(entities = [ScanHistoryEntity::class], version = 3, exportSchema = false)
abstract class ScanHistoryDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: ScanHistoryDatabase? = null

        fun getDatabase(context: Context): ScanHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScanHistoryDatabase::class.java,
                    "scan_history_database"
                )
                    .fallbackToDestructiveMigration() // Simplest migration strategy
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}