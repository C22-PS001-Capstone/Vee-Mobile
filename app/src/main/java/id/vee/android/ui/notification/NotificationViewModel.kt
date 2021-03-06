package id.vee.android.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.vee.android.domain.model.Notification
import id.vee.android.domain.usecase.VeeUseCase
import kotlinx.coroutines.launch

class NotificationViewModel(
    val veeUseCase: VeeUseCase
) : ViewModel() {
    private val _notificationResponse: MutableLiveData<List<Notification>> =
        MutableLiveData()
    val notificationResponse: LiveData<List<Notification>> = _notificationResponse

    fun getNotification() = viewModelScope.launch {
        veeUseCase.getNotification().collect {
            _notificationResponse.postValue(it)
        }
    }
}