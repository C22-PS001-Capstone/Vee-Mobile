package id.vee.android.data.local

import id.vee.android.domain.model.GasStations
import id.vee.android.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsInterface {
    fun getSettings(): Flow<Settings>
    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
    suspend fun setLocation(lat: Double, lon: Double)
    fun getStations(): Flow<List<GasStations>>
}