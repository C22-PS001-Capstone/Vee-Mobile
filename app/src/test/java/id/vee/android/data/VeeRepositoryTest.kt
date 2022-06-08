package id.vee.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.room.VeeDao
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
internal class VeeRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var veeDao: VeeDao
    private lateinit var veeRepository: VeeRepository
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource

    private val dummy = DataDummy

    @Before
    fun setUp() {
        apiService = FakeApiService()
        veeDao = FakeVeeDao()
        remoteDataSource = RemoteDataSource(apiService)
        localDataSource = LocalDataSource(veeDao)
        veeRepository = VeeRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun getUser() = runTest {

    }

    @Test
    fun getToken() {
    }

    @Test
    fun signup() {
    }

    @Test
    fun login() {
    }

    @Test
    fun loginGoogle() {
    }

    @Test
    fun refreshToken() {
    }

    @Test
    fun deleteUser() {
    }

    @Test
    fun deleteToken() {
    }

    @Test
    fun deleteTokenNetwork() {
    }

    @Test
    fun updateName() {
    }

    @Test
    fun updatePassword() {
    }

    @Test
    fun addPassword() {
    }

    @Test
    fun getGasStations() {
    }

    @Test
    fun getActivity() {
    }

    @Test
    fun deleteActivity() {
    }

    @Test
    fun getLocalStations() {
    }

    @Test
    fun getRobo() {
    }

    @Test
    fun insertNotification() {
    }

    @Test
    fun getNotification() {
    }

    @Test
    fun getForecast() {
    }

    @Test
    fun updateActivity() {
    }

    @Test
    fun userDetail() {
    }

    @Test
    fun saveToken() {
    }

    @Test
    fun saveUser() {
    }

    @Test
    fun insertActivity() {
    }
}