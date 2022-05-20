package id.vee.android.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.ThemeInterface
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User
import id.vee.android.domain.usecase.VeeUseCase
import kotlinx.coroutines.launch

open class GeneralViewModel constructor(
    private val useCase: VeeUseCase,
    private val pref: ThemeInterface
) : ViewModel() {
    private val _user: MutableLiveData<User> = MutableLiveData()
    val userResponse: LiveData<User> = _user

    private val _token: MutableLiveData<Token> = MutableLiveData()
    val tokenResponse: LiveData<Token> = _token

    private val _refresh: MutableLiveData<LoginResponse> = MutableLiveData()
    val refreshResponse: LiveData<LoginResponse> = _refresh

    private var _themeResponse: MutableLiveData<Boolean> = MutableLiveData()
    val themeResponse: LiveData<Boolean> = _themeResponse

    fun getUserData() = viewModelScope.launch {
        useCase.getUser().collect { values ->
            _user.value = values
        }
    }

    fun getToken() = viewModelScope.launch {
        useCase.getToken().collect { values ->
            _token.value = values
        }
    }

    fun refreshToken(refreshToken: String) = viewModelScope.launch {
        useCase.refreshToken(refreshToken).collect { values ->
            _refresh.value = values
        }
    }

    fun getThemeSettings() = viewModelScope.launch {
        pref.getThemeSetting().collect {
            _themeResponse.value = it
        }
    }
}