package id.vee.android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.local.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel(private val pref: ThemeInterface) : ViewModel() {
    private var _themeResponse: MutableLiveData<Boolean> = MutableLiveData()
    val themeResponse: LiveData<Boolean> = _themeResponse
    fun getThemeSettings() = viewModelScope.launch {
        pref.getThemeSetting().collect { values ->
            _themeResponse.postValue(values)
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        pref.saveThemeSetting(isDarkModeActive)
    }

}