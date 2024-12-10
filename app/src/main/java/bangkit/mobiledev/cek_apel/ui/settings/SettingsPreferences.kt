package bangkit.mobiledev.cek_apel.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_pref")

class SettingsPreferences(private val context: Context) {
    companion object {
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    }

    suspend fun setNotificationEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED] = isEnabled
        }
    }

    val isNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATION_ENABLED] ?: true
        }
}