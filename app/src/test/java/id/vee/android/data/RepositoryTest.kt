package id.vee.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummy = DataDummy

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    private lateinit var repository: VeeRepository

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        repository = VeeRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun `test get user`() = runTest {
        val expectedUser = dummy.getUserData()
        val user = dummy.getUserEntity()
        `when`(localDataSource.getUser()).thenReturn(flowOf((user)))
        repository.getUser().collect {
            assertEquals(expectedUser, it)
            assertNotNull(it)
            assertEquals(expectedUser.id, it.id)
        }
    }

    @Test
    fun `test get user and return null`() = runTest {
        `when`(localDataSource.getUser()).thenReturn(flowOf(null))
        repository.getUser().collect {
            assertEquals(null, it)
        }
    }

    @Test
    fun `test get token and return null`() = runTest {
        `when`(localDataSource.getToken()).thenReturn(flowOf(null))
        repository.getToken().collect {
            assertEquals(null, it)
        }
    }

    @Test
    fun `test get token`() = runTest {
        val expectedToken = dummy.getTokenData()
        val token = dummy.getTokenEntity()
        `when`(localDataSource.getToken()).thenReturn(flowOf((token)))
        repository.getToken().collect {
            assertEquals(expectedToken, it)
            assertNotNull(it)
            assertEquals(expectedToken.accessToken, it.accessToken)
        }
    }

    @Test
    fun `test signup`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.signup(
                "first",
                "last",
                "email@mail.com",
                "password",
                "password"
            )
        ).thenReturn(expected)
        repository.signup(
            "first",
            "last",
            "email@mail.com",
            "password",
            "password"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test login`() = runTest {
        val expected = dummy.getLoginResponse()
        `when`(
            remoteDataSource.login(
                "mail@mail.com",
                "password"
            )
        ).thenReturn(expected)
        repository.login(
            "mail@mail.com",
            "password"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test login google`() = runTest {
        val expected = dummy.getLoginResponse()
        `when`(
            remoteDataSource.loginGoogle(
                "token"
            )
        ).thenReturn(expected)
        repository.loginGoogle(
            "token"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test refresh token`() = runTest {
        val expected = dummy.getLoginResponse()
        `when`(
            remoteDataSource.refreshToken(
                "token"
            )
        ).thenReturn(expected)
        repository.refreshToken(
            "token"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test delete user`() = runTest {
        repository.deleteUser()
        verify(localDataSource).deleteUser()
    }

    @Test
    fun `test delete token`() = runTest {
        repository.deleteToken()
        verify(localDataSource).deleteToken()
    }

    @Test
    fun `test delete token network`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.logout(
                "token"
            )
        ).thenReturn(expected)
        repository.deleteTokenNetwork(
            "token"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test update name`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.updateName(
                "token",
                "first",
                "last"
            )
        ).thenReturn(expected)
        repository.updateName(
            "token",
            "first",
            "last"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test update password`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.updatePassword(
                "token",
                "oldPassword",
                "newPassword",
                "password"
            )
        ).thenReturn(expected)
        repository.updatePassword(
            "token",
            "oldPassword",
            "newPassword",
            "password"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `add password`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.addPassword(
                "token",
                "password",
                "password"
            )
        ).thenReturn(expected)
        repository.addPassword(
            "token",
            "password",
            "password"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `test gas stations`() = runTest {
        val expected = Resource.Success(dummy.gasStations())
        val listResponse = dummy.gasStationsResponse()
        val networkResponse = flow {
            emit(ApiResponse.Success(listResponse))
        }
        val localResponse = flow {
            emit(dummy.gasStationsEntity())
        }
        `when`(remoteDataSource.getGasStations("token", 6.000, 120.000)).thenReturn(networkResponse)
        `when`(localDataSource.getNearestGasStation()).thenReturn(localResponse)
        repository.getGasStations("token", 6.000, 120.000).onEach {
            assertNotNull(it)
            when (it) {
                is Resource.Success -> {
                    assertEquals(expected.data, it.data)
                    assertEquals(expected.data?.size, it.data?.size)
                }
                else -> {

                }
            }
        }.collect()
    }

    @Test
    fun `test delete activity`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.deleteActivity(
                "token",
                "id"
            )
        ).thenReturn(expected)
        repository.deleteActivity(
            "token",
            "id"
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `get local stations`() = runTest {
        val expected = dummy.gasStations()
        val localResponse = dummy.gasStationsEntity()
        `when`(localDataSource.getGasStations()).thenReturn(flowOf(localResponse))

        repository.getLocalStations().collect {
            assertEquals(expected, it)
            assertNotNull(it)
        }
    }

    @Test
    fun `get robo`() = runTest {
        val expected = dummy.getRobo()
        val localResponse = dummy.getRoboEntity()
        `when`(localDataSource.getRobo("-11-")).thenReturn(flowOf(localResponse))

        repository.getRobo("-11-").collect {
            assertEquals(expected, it)
            assertNotNull(it)
        }
    }

    @Test
    fun `insert notification`() = runTest {
        repository.insertNotification(dummy.notificationData())
        verify(localDataSource).insertNotification(dummy.notificationsEntity())
    }

    @Test
    fun `get notification`() = runTest {
        val expected = dummy.notificationsData()
        val localResponse = dummy.listNotificationEntities()
        `when`(localDataSource.getNotification()).thenReturn(flowOf(localResponse))

        repository.getNotification().collect {
            assertEquals(expected, it)
            assertNotNull(it)
        }
    }

    @Test
    fun `get forecast`() = runTest {
        val expected = dummy.getForecastResponse()
        `when`(
            remoteDataSource.getForecast(
                "token"
            )
        ).thenReturn(expected)
        repository.getForecast(
            "token",
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `update activity`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.updateActivity(
                "token",
                "id",
                "title",
                1000,
                100.2,
                100,
                6.00001,
                120.0001
            )
        ).thenReturn(expected)
        repository.updateActivity(
            "token",
            "id",
            "title",
            1000,
            100.2,
            100,
            6.00001,
            120.0001
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `get user detail`() = runTest {
        val expected = dummy.getUserDetail()
        val tokenData = dummy.getTokenData()
        val tokenEntity = dummy.getTokenEntity()
        `when`(
            remoteDataSource.userDetail(
                tokenEntity
            )
        ).thenReturn(expected)
        repository.userDetail(
            tokenData
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }

    @Test
    fun `save token`() = runTest {
        repository.saveToken(dummy.getTokenData())
        verify(localDataSource).saveToken(dummy.getTokenEntity())
        verify(localDataSource).deleteToken()
    }

    @Test
    fun `save user`() = runTest {
        repository.saveUser(dummy.getUserData())
        verify(localDataSource).saveUser(dummy.getUserEntity())
        verify(localDataSource).deleteUser()
    }

    @Test
    fun `insert activity`() = runTest {
        val expected = dummy.getBasicResponse()
        `when`(
            remoteDataSource.insertActivity(
                "token",
                date = "2020-01-01",
                1000,
                100.2,
                100,
                6.00001,
                120.0001
            )
        ).thenReturn(expected)
        repository.insertActivity(
            token = "token",
            date = "2020-01-01",
            1000,
            100.2,
            100,
            6.00001,
            120.0001
        ).collect {
            assertEquals(expected, it)
            assertNotNull(it)
            assertEquals(expected.status, it.status)
        }
    }
}