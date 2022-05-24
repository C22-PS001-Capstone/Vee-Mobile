package id.vee.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.Resource
import id.vee.android.data.local.ThemeInterface
import id.vee.android.domain.model.Activity
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref) {
    private val _activityResponse: MutableLiveData<Resource<List<Activity>>> = MutableLiveData()
    val activityResponse: LiveData<Resource<List<Activity>>> = _activityResponse

    fun getActivity(token: String) = viewModelScope.launch {
        useCase.getActivity(token).collect {
            _activityResponse.postValue(it)
        }
    }
}