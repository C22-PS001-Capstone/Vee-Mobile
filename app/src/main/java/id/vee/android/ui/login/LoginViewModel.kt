package id.vee.android.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.entity.TokenEntity
import id.vee.android.data.local.entity.UserEntity
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import kotlinx.coroutines.launch

class LoginViewModel constructor(private val repository: VeeDataSource) : ViewModel() {
    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse> = _response

    private val _responseDetail = MutableLiveData<UserDetailResponse>()
    val responseDetail: LiveData<UserDetailResponse> = _responseDetail
    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).collect { values ->
            _response.value = values
        }
    }

    fun userDetail(data: TokenEntity) = viewModelScope.launch {
        repository.userDetail(data).collect { values ->
            Log.d(TAG, "userDetail: $values")
            _responseDetail.value = values
        }
    }

    fun saveToken(data: TokenEntity) = viewModelScope.launch {
        repository.saveToken(data)
    }

    fun saveUser(user: UserEntity) = viewModelScope.launch {
        repository.saveUser(user)
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}