package id.vee.android.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.vee.android.data.VeeDataSource
import id.vee.android.di.Injection
import id.vee.android.ui.GeneralViewModel
import id.vee.android.ui.activity.ActivityViewModel
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.ui.login.LoginViewModel
import id.vee.android.ui.notification.NotificationViewModel
import id.vee.android.ui.profile.ProfileViewModel
import id.vee.android.ui.signup.SignupViewModel

class ViewModelFactory private constructor(
    private val mVeeRepository: VeeDataSource
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> NotificationViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> ActivityViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(GeneralViewModel::class.java) -> GeneralViewModel(
                mVeeRepository
            ) as T
           /* modelClass.isAssignableFrom(ThemeViewModel::class.java) -> ThemeViewModel(
                mThemePreferences
            ) as T*/
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}