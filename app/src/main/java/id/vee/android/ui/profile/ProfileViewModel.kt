package id.vee.android.ui.profile

import androidx.lifecycle.viewModelScope
import id.vee.android.data.VeeDataSource
import id.vee.android.ui.GeneralViewModel
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
    private val repository: VeeDataSource
) : GeneralViewModel(repository) {
    fun logout() = viewModelScope.launch {
        repository.deleteUser()
        repository.deleteToken()
    }
}