package id.vee.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel constructor(private val repository: VeeDataSource) : ViewModel() {
    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse> = _response
    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).collect { values ->
            _response.value = values
        }
    }

    fun saveUser(data: TokenEntity) = viewModelScope.launch {
        repository.saveUser(data)
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}