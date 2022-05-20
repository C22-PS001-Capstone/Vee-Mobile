package id.vee.android.data.local

import kotlinx.coroutines.flow.Flow

interface ThemeInterface{
    fun getThemeSetting(): Flow<Boolean>
    suspend fun saveThemeSetting(isDarkModeActive: Boolean)
}