package id.vee.android.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref) {
    private val _logoutResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    private val _updateNameResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    private val _updatePasswordResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val logoutResponse: LiveData<BasicResponse> = _logoutResponse


    fun logout(token: String) = viewModelScope.launch {
        useCase.deleteTokenNetwork(token).collect { values ->
            _logoutResponse.postValue(values)
        }
        useCase.deleteUser()
        useCase.deleteToken()
    }

    fun updateName(token: String, firstName: String, lastName: String) = viewModelScope.launch {
        useCase.updateName(token, firstName, lastName).collect { values ->
            _updateNameResponse.postValue(values)
        }
    }

    fun updatePassword(
        token: String,
        passwordCurrent: String,
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch {
        useCase.updatePassword(token, passwordCurrent, password, passwordConfirm)
            .collect { values ->
                _updatePasswordResponse.postValue(values)
            }
    }
}