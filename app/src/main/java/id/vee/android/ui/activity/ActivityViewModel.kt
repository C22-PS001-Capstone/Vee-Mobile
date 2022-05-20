package id.vee.android.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ActivityViewModel constructor(
    private val useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref) {
    private val _actionResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val actionResponse: LiveData<BasicResponse> = _actionResponse

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
                    _actionResponse.value = values
                }
        }
}
