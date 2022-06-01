package id.vee.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.Resource
import id.vee.android.data.local.SettingsInterface
import id.vee.android.domain.model.Activity
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.model.Robo
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel constructor(
    private val useCase: VeeUseCase,
    pref: SettingsInterface
) : GeneralViewModel(useCase, pref) {
    private val _activityResponse: MutableLiveData<Resource<List<Activity>>> = MutableLiveData()
    val activityResponse: LiveData<Resource<List<Activity>>> = _activityResponse
    private val _gasStationsResponse: MutableLiveData<Resource<List<GasStations>>> =
        MutableLiveData()
    val gasStationsResponse: LiveData<Resource<List<GasStations>>> = _gasStationsResponse
    private val _robo: MutableLiveData<List<Robo>> = MutableLiveData()
    val roboResponse: LiveData<List<Robo>> = _robo

    fun getActivity(token: String) = viewModelScope.launch {
        useCase.getActivity(token).collect {
            _activityResponse.postValue(it)
        }
    }

    fun getRobo(month: String) = viewModelScope.launch {
        useCase.getRobo(month).collect {
            _robo.postValue(it)
        }
    }

    fun getGasStations(token: String, lat: Double, lon: Double) = viewModelScope.launch {
        Timber.d("Gas Station: $token $lat $lon")
        useCase.getGasStations(token, lat, lon).collect {
            _gasStationsResponse.postValue(it)
        }
    }
}