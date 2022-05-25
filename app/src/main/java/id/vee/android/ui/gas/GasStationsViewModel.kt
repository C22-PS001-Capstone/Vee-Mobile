package id.vee.android.ui.gas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.remote.response.GasStationsResponse
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class GasStationsViewModel constructor(
    private val useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref) {
    private val _gasStationsResponse: MutableLiveData<GasStationsResponse> = MutableLiveData()
    val gasStationsResponse = _gasStationsResponse

    fun getGasStaions(lat: Double, lon: Double) = viewModelScope.launch {
        useCase.getGasStations(lat, lon).collect {
            _gasStationsResponse.postValue(it)
        }
    }
}