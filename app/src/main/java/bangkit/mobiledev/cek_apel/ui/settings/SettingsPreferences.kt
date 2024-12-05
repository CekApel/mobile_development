package bangkit.mobiledev.cek_apel.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create an extension property on Context for DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_pref")

class SettingsPreferences(private val context: Context) {
    companion object {
        // Define keys for different settings
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    }

    // Save notification setting
    suspend fun setNotificationEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = isEnabled
        }
    }

    // Read notification setting with default value true
    val isNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATION_ENABLED] ?: true
        }
}