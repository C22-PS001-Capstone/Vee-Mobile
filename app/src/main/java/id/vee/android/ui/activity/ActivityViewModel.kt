package id.vee.android.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ActivityViewModel constructor(
    private val repository: VeeDataSource
) : GeneralViewModel(repository) {
    private val _actionResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val actionResponse: LiveData<BasicResponse> = _actionResponse

    fun insertActivity(token: String, date: String, distance: Int, litre: Int, expense: Int) =
        viewModelScope.launch {
            repository.insertActivity(token, date, distance, litre, expense).collect { values ->
                _actionResponse.value = values
            }
        }
}
