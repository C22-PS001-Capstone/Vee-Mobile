package id.vee.android.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.LocalDataSource
import id.vee.android.data.local.room.VeeDao
import id.vee.android.data.remote.RemoteDataSource
import id.vee.android.data.remote.network.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        val expectedData = dummy.getUserEntity()
        localDataSource.saveUser(expectedData)
        localDataSource.getUser().collect { actualUser ->
            assertNotNull(actualUser)
            assertEquals(expectedData, actualUser)
            assertEquals(expectedData.id, actualUser?.id)
        }
    }

    @Test
    fun getToken() = runTest {
        val expectedData = dummy.getTokenEntity()
        localDataSource.saveToken(expectedData)
        localDataSource.getToken().collect { actualUser ->
            assertNotNull(actualUser)
            assertEquals(expectedData, actualUser)
            assertEquals(expectedData.accessToken, actualUser?.accessToken)
        }
    }

    @Test
    fun signup() = runTest {
        val expected = dummy.getBasicResponse()
        val actual = flowOf(
            remoteDataSource.signup(
                "first",
                "last",
                "mail@mail.com",
                "password",
                "password"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun login() = runTest {
        val expected = dummy.getLoginResponse()
        val actual = flowOf(
            remoteDataSource.login(
                "email@mail.com",
                "password"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun loginGoogle() = runTest {
        val expected = dummy.getLoginResponse()
        val actual = flowOf(
            remoteDataSource.loginGoogle(
                "token"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun refreshToken() = runTest {
        val expected = dummy.getLoginResponse()
        val actual = flowOf(
            remoteDataSource.refreshToken(
                "token"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun deleteUser() = runTest {
        val sampleUser = dummy.getUserEntity()
        localDataSource.saveUser(sampleUser)
        localDataSource.deleteUser()
        localDataSource.getUser().collect { actualUser ->
            assertNull(actualUser)
        }
    }

    @Test
    fun deleteToken() = runTest {
        val sampleToken = dummy.getTokenEntity()
        localDataSource.saveToken(sampleToken)
        localDataSource.deleteToken()
        localDataSource.getToken().collect { actualToken ->
            assertNull(actualToken)
        }
    }

    @Test
    fun deleteTokenNetwork() = runTest {
        val expected = dummy.getBasicResponse()
        val actual = flowOf(
            remoteDataSource.logout(
                "token"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun updateName() = runTest {
        val expected = dummy.getBasicResponse()
        val actual = flowOf(
            remoteDataSource.updateName(
                "token",
                "first",
                "last"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun updatePassword() = runTest {
        val expected = dummy.getBasicResponse()
        val actual = flowOf(
            remoteDataSource.updatePassword(
                "token",
                "old",
                "new",
                "new"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun addPassword() = runTest {
        val expected = dummy.getBasicResponse()
        val actual = flowOf(
            remoteDataSource.addPassword(
                "token",
                "password",
                "password"
            )
        )
        actual.collect {
            assertNotNull(it)
            assertEquals(expected, it)
        }
    }

    @Test
    fun getGasStations() = runTest {
        // TODO because using network bound
    }

    @Test
    fun getActivity() = runTest {
        // TODO because it will updated soon
    }

    @Test
    fun deleteActivity() = runTest {
        val sampleToken = dummy.getListEntityResponse()
        localDataSource.insertActivity(sampleToken)
        localDataSource.deleteActivities()
        localDataSource.getActivity("-00-").collect{
            assertEquals(0, it.size)
        }
    }

    @Test
    fun getLocalStations() = runTest {
    }

    @Test
    fun getRobo() = runTest {
    }

    @Test
    fun insertNotification() = runTest {
    }

    @Test
    fun getNotification() = runTest {
    }

    @Test
    fun getForecast() = runTest {
    }

    @Test
    fun updateActivity() = runTest {
    }

    @Test
    fun userDetail() = runTest {
    }

    @Test
    fun saveToken() = runTest {
    }

    @Test
    fun saveUser() = runTest {
    }

    @Test
    fun insertActivity() = runTest {
    }
}