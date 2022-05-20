package id.vee.android.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.domain.usecase.VeeUseCase
import kotlinx.coroutines.launch

class SignupViewModel constructor(
    private val useCase: VeeUseCase
) : ViewModel() {
    private var _response: MutableLiveData<BasicResponse> = MutableLiveData()
    val response: LiveData<BasicResponse> = _response
    fun signup(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch {
        useCase.signup(firstName, lastName, email, password, passwordConfirm).collect { values ->
            _response.postValue(values)
        }
    }
}