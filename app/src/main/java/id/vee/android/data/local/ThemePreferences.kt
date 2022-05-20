package id.vee.android.data.local

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) : ThemeInterface {

    private val theme = booleanPreferencesKey("theme_setting")

    override fun getThemeSetting(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[theme] ?: false
        }
    }

    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[theme] = isDarkModeActive
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(context: Context): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }

}