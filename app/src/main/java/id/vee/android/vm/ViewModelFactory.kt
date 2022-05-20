package id.vee.android.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.data.local.ThemePreferences
import id.vee.android.di.Injection
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import id.vee.android.ui.activity.ActivityViewModel
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.ui.login.LoginViewModel
import id.vee.android.ui.notification.NotificationViewModel
import id.vee.android.ui.profile.ProfileViewModel
import id.vee.android.ui.profile.ThemeViewModel
import id.vee.android.ui.signup.SignupViewModel

class ViewModelFactory private constructor(
    private val useCase: VeeUseCase,
    private val pref: ThemePreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                useCase, pref
            ) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                useCase
            ) as T
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(
                useCase
            ) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                useCase, pref
            ) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(
                useCase
            ) as T
            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> ActivityViewModel(
                useCase, pref
            ) as T
            modelClass.isAssignableFrom(GeneralViewModel::class.java) -> GeneralViewModel(
                useCase, pref
            ) as T
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> ThemeViewModel(
                pref
             ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: ThemePreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideVeeRepository(context), pref)
            }.also { instance = it }
    }
}