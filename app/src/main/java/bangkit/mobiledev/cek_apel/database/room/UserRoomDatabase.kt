package bangkit.mobiledev.cek_apel.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bangkit.mobiledev.cek_apel.database.entity.UserProfile


@Database(entities = [UserProfile::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "cek_apel_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}