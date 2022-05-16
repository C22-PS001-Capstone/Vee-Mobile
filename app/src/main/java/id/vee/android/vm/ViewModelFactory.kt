package id.vee.android.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.vee.android.data.VeeDataSource
import id.vee.android.di.Injection
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.ui.login.LoginViewModel
import id.vee.android.ui.notification.NotificationViewModel
import id.vee.android.ui.profile.ProfileViewModel
import id.vee.android.ui.signup.SignupViewModel
import id.vee.android.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val mVeeRepository: VeeDataSource,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> SplashViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> SplashViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> SplashViewModel(
                mVeeRepository
            ) as T
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SplashViewModel(
                mVeeRepository
            ) as T
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