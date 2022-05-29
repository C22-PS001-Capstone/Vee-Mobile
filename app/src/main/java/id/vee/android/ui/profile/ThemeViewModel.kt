package id.vee.android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.SettingsInterface
import kotlinx.coroutines.launch

class ThemeViewModel(private val pref: SettingsInterface) : ViewModel() {
    private var _themeResponse: MutableLiveData<Boolean> = MutableLiveData()
    val themeResponse: LiveData<Boolean> = _themeResponse
    fun getThemeSettings() = viewModelScope.launch {
        pref.getSettings().collect { values ->
            _themeResponse.postValue(values.theme)
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        pref.saveThemeSetting(isDarkModeActive)
    }

}