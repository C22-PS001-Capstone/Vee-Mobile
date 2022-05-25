package id.vee.android.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.Resource
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.domain.model.Activity
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ActivityViewModel constructor(
    private val useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref) {
    private val _actionResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val actionResponse: LiveData<BasicResponse> = _actionResponse

    private val _activityResponse: MutableLiveData<Resource<List<Activity>>> = MutableLiveData()
    val activityResponse: LiveData<Resource<List<Activity>>> = _activityResponse

    fun insertActivity(
        token: String,
        date: String,
        distance: Int,
        litre: Int,
        expense: Int,
        lat: Double,
        long: Double
    ) =
        viewModelScope.launch {
            useCase.insertActivity(token, date, distance, litre, expense, lat, long)
                .collect { values ->
                    _actionResponse.postValue(values)
                }
        }

    fun getActivity(token: String) = viewModelScope.launch {
        useCase.getActivity(token).collect {
            _activityResponse.postValue(it)
        }
    }
}
