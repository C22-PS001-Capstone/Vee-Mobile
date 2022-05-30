package id.vee.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.SettingsInterface
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val useCase: VeeUseCase,
    pref: SettingsInterface
) : GeneralViewModel(useCase, pref) {

    private val _stations: MutableLiveData<List<GasStations>> = MutableLiveData()
    val stationsResponse: LiveData<List<GasStations>> = _stations

    fun updateLocation(latitude: Double, longitude: Double) = viewModelScope.launch {
        pref.setLocation(latitude, longitude)
    }

    fun getLocalStations() = viewModelScope.launch {
        useCase.getLocalStations().collect {
            _stations.postValue(it)
        }
    }
}