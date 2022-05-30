package id.vee.android.ui.main

import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.SettingsInterface
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    useCase: VeeUseCase,
    pref: SettingsInterface
) : GeneralViewModel(useCase, pref) {
    fun updateLocation(latitude: Double, longitude: Double) = viewModelScope.launch {
        pref.setLocation(latitude, longitude)
    }
}