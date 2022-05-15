package id.vee.android.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: VeeDataSource
) : ViewModel() {
    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val userResponse: LiveData<UserEntity> = _user

    fun getUser() = viewModelScope.launch {
        repository.getUser().collect { values ->
            _user.value = values
        }
    }
}