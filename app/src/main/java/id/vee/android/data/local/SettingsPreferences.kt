package id.vee.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import id.vee.android.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences(private val context: Context) : SettingsInterface {

    private val theme = booleanPreferencesKey("theme_setting")
    private val lat_setting = doublePreferencesKey("lat_setting")
    private val lon_setting = doublePreferencesKey("lng_setting")

    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            Settings(
                preferences[theme] ?: false,
                preferences[lat_setting] ?: 0.0,
                preferences[lon_setting] ?: 0.0,
            )
        }
    }

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[theme] = isDarkModeActive
        }
    }

    override suspend fun setLocation(lat: Double, lon: Double) {
        context.dataStore.edit { preferences ->
            preferences[lat_setting] = lat
            preferences[lon_setting] = lon
        }
    }
}