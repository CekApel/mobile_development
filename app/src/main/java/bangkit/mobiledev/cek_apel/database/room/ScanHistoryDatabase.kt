package bangkit.mobiledev.cek_apel.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bangkit.mobiledev.cek_apel.database.entity.ScanHistoryEntity

@Database(entities = [ScanHistoryEntity::class], version = 4, exportSchema = false)
abstract class ScanHistoryDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCES: MutableMap<String, ScanHistoryDatabase> = mutableMapOf()

        fun getDatabase(context: Context, userId: String): ScanHistoryDatabase {
            return INSTANCES[userId] ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScanHistoryDatabase::class.java,
                    "scan_history_database_$userId"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCES[userId] = instance
                instance
            }
        }
    }
}