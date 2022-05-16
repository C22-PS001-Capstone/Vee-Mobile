package id.vee.android.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.data.remote.response.BasicResponse
import kotlinx.coroutines.launch

class SignupViewModel constructor(private val repository: VeeDataSource) : ViewModel() {
    private var _response: MutableLiveData<BasicResponse> = MutableLiveData()
    val response: LiveData<BasicResponse> = _response
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch {
        repository.signup(firstName, lastName, email, password, passwordConfirm).collect { values->
            _response.value = values
        }
    }
}