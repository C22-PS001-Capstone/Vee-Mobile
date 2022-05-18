package id.vee.android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

open class GeneralViewModel constructor(
    private val repository: VeeDataSource
) : ViewModel() {
    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val userResponse: LiveData<UserEntity> = _user

    private val _token: MutableLiveData<TokenEntity> = MutableLiveData()
    val tokenResponse: LiveData<TokenEntity> = _token

    private val _refresh: MutableLiveData<LoginResponse> = MutableLiveData()
    val refreshResponse: LiveData<LoginResponse> = _refresh

    fun getUserData() = viewModelScope.launch {
        repository.getUser().collect { values ->
            _user.value = values
        }
    }

    fun getToken() = viewModelScope.launch {
        repository.getToken().collect { values ->
            _token.value = values
        }
    }

    fun refreshToken(refreshToken: String) = viewModelScope.launch {
        repository.refreshToken(refreshToken).collect { values ->
            _refresh.value = values
        }
    }
}