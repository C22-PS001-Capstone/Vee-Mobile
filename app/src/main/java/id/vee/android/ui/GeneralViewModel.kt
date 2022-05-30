package id.vee.android.ui

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.local.SettingsInterface
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.domain.model.Token
import id.vee.android.domain.model.User
import id.vee.android.domain.usecase.VeeUseCase
import kotlinx.coroutines.launch

open class GeneralViewModel constructor(
    private val useCase: VeeUseCase,
    val pref: SettingsInterface
) : ViewModel() {
    private val _user: MutableLiveData<User> = MutableLiveData()
    val userResponse: LiveData<User> = _user

    private val _token: MutableLiveData<Token> = MutableLiveData()
    val tokenResponse: LiveData<Token> = _token

    private val _refresh: MutableLiveData<LoginResponse> = MutableLiveData()
    val refreshResponse: LiveData<LoginResponse> = _refresh

    private var _themeResponse: MutableLiveData<Boolean> = MutableLiveData()
    val themeResponse: LiveData<Boolean> = _themeResponse

    private var _locationResponse: MutableLiveData<Location> = MutableLiveData()
    val locationResponse: LiveData<Location> = _locationResponse

    fun getUserData() = viewModelScope.launch {
        useCase.getUser().collect { values ->
            _user.postValue(values)
        }
    }

    fun saveToken(data: Token) = viewModelScope.launch {
        useCase.saveToken(data)
    }

    fun getToken() = viewModelScope.launch {
        useCase.getToken().collect { values ->
            _token.postValue(values)
        }
    }

    fun refreshToken(refreshToken: String) = viewModelScope.launch {
        useCase.refreshToken(refreshToken).collect { values ->
            _refresh.postValue(values)
        }
    }

    fun getThemeSettings() = viewModelScope.launch {
        pref.getSettings().collect { values ->
            _themeResponse.postValue(values.theme)
        }
    }

    fun getLiveLocation() = viewModelScope.launch {
        pref.getSettings().collect { values ->
            _locationResponse.postValue(Location("Live").apply{
                latitude = values.latitude
                longitude = values.longitude
            })
        }
    }
}