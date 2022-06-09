package id.vee.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.data.local.room.VeeDao
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var veeDao: VeeDao

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSource(apiService)
        localDataSource = LocalDataSource(veeDao)
    }

    @Test
    fun `when signup return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).signup(
            "first",
            "last",
            "email",
            "password",
            "password"
        )
        val actual =
            flowOf(remoteDataSource.signup("first", "last", "email", "password", "password"))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when login return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).login(
            "email",
            "password"
        )
        val actual =
            flowOf(remoteDataSource.login("email", "password"))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when refresh token return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).refreshToken(
            "refreshToken"
        )
        val actual =
            flowOf(remoteDataSource.refreshToken("refreshToken"))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when get user detail return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).userDetail(
            "Bearer " + dummy.getTokenEntity().accessToken
        )
        val actual =
            flowOf(remoteDataSource.userDetail(dummy.getTokenEntity()))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }
}