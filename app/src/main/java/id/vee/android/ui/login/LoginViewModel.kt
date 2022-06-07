package id.vee.android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User
import id.vee.android.domain.usecase.VeeUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel constructor(private val useCase: VeeUseCase) : ViewModel() {
    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse> = _response

    private val _responseDetail = MutableLiveData<UserDetailResponse>()
    val responseDetail: LiveData<UserDetailResponse> = _responseDetail
    fun login(email: String, password: String) = viewModelScope.launch {
        useCase.login(email, password).collect { values ->
            _response.postValue(values)
        }
    }

    fun userDetail(data: Token) = viewModelScope.launch {
        useCase.userDetail(data).collect { values ->
            _responseDetail.postValue(values)
        }
    }

    fun saveToken(data: Token) = viewModelScope.launch {
        useCase.saveToken(data)
    }

    fun saveUser(user: User) = viewModelScope.launch {
        useCase.saveUser(user)
    }

    fun loginGoogle(token: String) = viewModelScope.launch {
        useCase.loginGoogle(token).collect { values ->
            Timber.d("loginGoogle $values")
            _response.postValue(values)
        }
    }
}