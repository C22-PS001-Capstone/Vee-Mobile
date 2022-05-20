package id.vee.android.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences private constructor(private val dataStore: DataStore<Preferences>): ThemeInterface {

    private val theme = booleanPreferencesKey("theme_setting")

    override fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[theme] ?: false
        }
    }

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[theme] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}