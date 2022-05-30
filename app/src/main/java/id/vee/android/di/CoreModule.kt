package id.vee.android.di

import androidx.room.Room
import id.vee.android.BuildConfig
import id.vee.android.data.VeeRepository
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.SettingsInterface
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.data.local.room.VeeDatabase
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiService
import id.vee.android.domain.repository.VeeDataSource
import id.vee.android.domain.usecase.VeeInteractor
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel
import id.vee.android.ui.activity.ActivityViewModel
import id.vee.android.ui.gas.GasStationsViewModel
import id.vee.android.ui.home.HomeViewModel
import id.vee.android.ui.login.LoginViewModel
import id.vee.android.ui.main.MainViewModel
import id.vee.android.ui.notification.NotificationViewModel
import id.vee.android.ui.profile.ProfileViewModel
import id.vee.android.ui.profile.ThemeViewModel
import id.vee.android.ui.signup.SignupViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<VeeDatabase>().veeDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            VeeDatabase::class.java, "Vee.db"
        ).fallbackToDestructiveMigration().build()
    }
}
val networkModule = module {
    single {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<VeeDataSource> { VeeRepository(get(), get()) }
}
val preferencesModule = module {
    factory<SettingsInterface> { SettingsPreferences(androidContext()) }
}

val useCaseModule = module {
    factory<VeeUseCase> { VeeInteractor(get()) }
}
val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { ActivityViewModel(get(), get()) }
    viewModel { GeneralViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { GasStationsViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}