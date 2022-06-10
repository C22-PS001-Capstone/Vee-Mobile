package id.vee.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.data.local.room.VeeDao
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiResponse
import id.vee.android.data.remote.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
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
class RemoteDataSourceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummy = DataDummy

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var remoteDataSource: RemoteDataSource

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSource(apiService)
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

    @Test
    fun `when login google return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).loginGoogle(
            "Bearer googleToken"
        )
        val actual =
            flowOf(remoteDataSource.loginGoogle("googleToken"))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when insert activity return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).insertActivity(
            "Bearer " + dummy.getTokenEntity().accessToken,
            "2019-01-01",
            2000,
            100.2,
            100000,
            6.0002,
            -120.0002,
        )
        val actual =
            flowOf(
                remoteDataSource.insertActivity(
                    dummy.getTokenEntity().accessToken,
                    "2019-01-01",
                    2000,
                    100.2,
                    100000,
                    6.0002,
                    -120.0002,
                )
            )
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when logout return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).logout(
            "Bearer " + dummy.getTokenEntity().accessToken
        )
        val actual =
            flowOf(remoteDataSource.logout("Bearer " + dummy.getTokenEntity().accessToken))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when update name return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).updateName(
            "Bearer " + dummy.getTokenEntity().accessToken,
            "first",
            "last"
        )
        val actual =
            flowOf(remoteDataSource.updateName(dummy.getTokenEntity().accessToken, "first", "last"))
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when update password return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).updatePassword(
            "Bearer " + dummy.getTokenEntity().accessToken,
            "oldPassword",
            "newPassword",
            "newPassword"
        )
        val actual =
            flowOf(
                remoteDataSource.updatePassword(
                    dummy.getTokenEntity().accessToken,
                    "oldPassword",
                    "newPassword",
                    "newPassword"
                )
            )
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when add password return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).addPassword(
            "Bearer " + dummy.getTokenEntity().accessToken,
            "password",
            "password"
        )
        val actual =
            flowOf(
                remoteDataSource.addPassword(
                    dummy.getTokenEntity().accessToken,
                    "password",
                    "password"
                )
            )
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when get gas station return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).getGasStations(
            "Bearer " + dummy.getTokenEntity().accessToken,
            6.12312,
            120.123123
        )
        val actual = remoteDataSource.getGasStations(
            dummy.getTokenEntity().accessToken,
            6.12312,
            120.123123
        )

        actual.collect {
            assertTrue(it is ApiResponse.Error)
        }
    }

    @Test
    fun `when update activity return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).updateActivity(
            "Bearer " + dummy.getTokenEntity().accessToken,
            "id-12312312",
            "2019-01-01",
            100,
            100.2,
            100000,
            6.00123,
            120.123123
        )
        val actual =
            flowOf(
                remoteDataSource.updateActivity(
                    token = dummy.getTokenEntity().accessToken,
                    id = "id-12312312",
                    date = "2019-01-01",
                    distance = 100,
                    litre = 100.2,
                    expense = 100000,
                    lat = 6.00123,
                    long = 120.123123
                )
            )
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when delete activity return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).deleteActivity(
            "id-12312312",
            "Bearer " + dummy.getTokenEntity().accessToken,
        )
        val actual =
            flowOf(
                remoteDataSource.deleteActivity(
                    dummy.getTokenEntity().accessToken,
                    "id-12312312"
                )
            )
        actual.collect {
            assertEquals(it.status, "error")
            assertEquals(it.data, null)
        }
    }

    @Test
    fun `when get forecast return error`() = runTest {
        doReturn(Exception("error")).`when`(apiService).getForecast(
            "Bearer " + dummy.getTokenEntity().accessToken,
        )

        val actual = remoteDataSource.getForecast(
            dummy.getTokenEntity().accessToken,
        )
        assertEquals(actual.status, "error")
        assertEquals(actual.data, null)
    }
}