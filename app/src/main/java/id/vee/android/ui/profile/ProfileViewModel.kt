package id.vee.android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val repository: VeeDataSource
) : GeneralViewModel(repository) {
    private val _logoutResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val logoutResponse: LiveData<BasicResponse> = _logoutResponse


    fun logout(token: String) = viewModelScope.launch {
        repository.deleteTokenNetwork(token).collect { values ->
            _logoutResponse.value = values
        }
//        repository.deleteUser()
//        repository.deleteToken()
    }
}