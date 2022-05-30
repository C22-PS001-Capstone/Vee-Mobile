package id.vee.android.ui.gas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.Resource
import id.vee.android.data.local.SettingsInterface
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class GasStationsViewModel constructor(
    private val useCase: VeeUseCase,
    pref: SettingsInterface
) : GeneralViewModel(useCase, pref) {
    private val _gasStationsResponse: MutableLiveData<Resource<List<GasStations>>> =
        MutableLiveData()
    val gasStationsResponse: LiveData<Resource<List<GasStations>>> = _gasStationsResponse

    fun getGasStations(token: String, lat: Double, lon: Double) = viewModelScope.launch {
        Timber.d("Gas Station: $token $lat $lon")
        useCase.getGasStations(token, lat, lon).collect {
            _gasStationsResponse.postValue(it)
        }
    }
}