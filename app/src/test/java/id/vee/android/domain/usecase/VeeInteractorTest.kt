package id.vee.android.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.Resource
import id.vee.android.data.VeeRepository
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.repository.VeeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class VeeInteractorTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummy = DataDummy

    @Mock
    private lateinit var repository: VeeDataSource
    private lateinit var interactor: VeeInteractor

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        interactor = VeeInteractor(repository)
    }

    @Test
    fun `get User and success`() = runTest {
        val expectedResult = dummy.getUserData()
        `when`(repository.getUser()).thenReturn(flowOf(expectedResult))
        interactor.getUser().collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.firstName, actualResult?.firstName)
        }
        verify(repository).getUser()
    }

    @Test
    fun `signup and success`() = runTest {
        val expectedSignup = dummy.getBasicResponse()
        `when`(
            repository.signup(
                firstName = "firstName",
                lastName = "lastName",
                email = "email@mail.com",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedSignup))
        interactor.signup(
            firstName = "firstName",
            lastName = "lastName",
            email = "email@mail.com",
            password = "password",
            passwordConfirm = "passwordConfirm"
        ).collect { actualSignup ->
            assertNotNull(actualSignup)
            assertEquals(expectedSignup, actualSignup)
            assertEquals(expectedSignup.status, actualSignup.status)
        }
        verify(repository).signup(
            firstName = "firstName",
            lastName = "lastName",
            email = "email@mail.com",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
    }

    @Test
    fun `login and success`() = runTest {
        val expectedResult = dummy.getLoginResponse()
        `when`(repository.login("email@mail.com", "password")).thenReturn(flowOf(expectedResult))
        interactor.login("email@mail.com", "password").collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).login("email@mail.com", "password")
    }

    @Test
    fun `login with google and success`() = runTest {
        val expectedResult = dummy.getLoginResponse()

        `when`(repository.loginGoogle("token")).thenReturn(flowOf(expectedResult))
        interactor.loginGoogle("token").collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).loginGoogle("token")
    }

    @Test
    fun `get refresh token and success`() = runTest {
        val expectedResult = dummy.getLoginResponse()

        `when`(repository.refreshToken("token")).thenReturn(flowOf(expectedResult))
        interactor.refreshToken("token").collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).refreshToken("token")
    }

    @Test
    fun `delete user and success`() = runTest {
        interactor.deleteUser()
        verify(repository).deleteUser()
    }

    @Test
    fun `delete token and success`() = runTest {
        interactor.deleteToken()
        verify(repository).deleteToken()
    }

    @Test
    fun `delete token to network and success`() = runTest {
        val expectedResponse = dummy.getBasicResponse()
        `when`(repository.deleteTokenNetwork("token")).thenReturn(flowOf(expectedResponse))
        interactor.deleteTokenNetwork("token").collect { actualResponse ->
            assertNotNull(actualResponse)
            assertEquals(expectedResponse, actualResponse)
            assertEquals(expectedResponse.status, actualResponse.status)

        }
        verify(repository).deleteTokenNetwork("token")
    }

    @Test
    fun `get user detail and success`() = runTest {
        val expectedResult = dummy.getUserDetail()
        val dummyToken = dummy.getTokenData()
        `when`(repository.userDetail(dummyToken)).thenReturn(flowOf(expectedResult))
        interactor.userDetail(dummyToken).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).userDetail(dummyToken)
    }

    @Test
    fun `save token and success`() = runTest {
        val dummyData = dummy.getTokenData()
        interactor.saveToken(dummyData)
        verify(repository).saveToken(dummyData)
    }

    @Test
    fun `save user and success`() = runTest {
        val dummyData = dummy.getUserData()
        interactor.saveUser(dummyData)
        verify(repository).saveUser(dummyData)
    }

    @Test
    fun `insertActivity and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.insertActivity(
                token = "token",
                date = "2020-01-01",
                distance = 233,
                litre = 233,
                expense = 233,
                lat = 6.123123132,
                long = -120.123123
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.insertActivity(
            token = "token",
            date = "2020-01-01",
            distance = 233,
            litre = 233,
            expense = 233,
            lat = 6.123123132,
            long = -120.123123
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).insertActivity(
            token = "token",
            date = "2020-01-01",
            distance = 233,
            litre = 233,
            expense = 233,
            lat = 6.123123132,
            long = -120.123123
        )
    }

    @Test
    fun `update name and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.updateName(
                token = "token",
                firstName = "firstName",
                lastName = "lastName"
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.updateName(
            token = "token",
            firstName = "firstName",
            lastName = "lastName"
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).updateName(
            token = "token",
            firstName = "firstName",
            lastName = "lastName"
        )
    }

    @Test
    fun `update password and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.updatePassword(
                token = "token",
                passwordCurrent = "password",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.updatePassword(
            token = "token",
            passwordCurrent = "password",
            password = "password",
            passwordConfirm = "passwordConfirm"
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).updatePassword(
            token = "token",
            passwordCurrent = "password",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
    }

    @Test
    fun `add password and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.addPassword(
                token = "token",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.addPassword(
            token = "token",
            password = "password",
            passwordConfirm = "passwordConfirm"
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).addPassword(
            token = "token",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
    }

    @Test
    fun `delete activity and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.deleteActivity(
                accessToken = "token",
                id = "id"
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.deleteActivity(
            accessToken = "token",
            id = "id"
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).deleteActivity(
            accessToken = "token",
            id = "id"
        )
    }

    @Test
    fun `update activity and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            repository.updateActivity(
                token = "token",
                id = "id",
                date = "2020-01-01",
                distance = 233,
                litre = 233,
                expense = 233,
                lat = 6.123123132,
                long = -120.123123
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.updateActivity(
            token = "token",
            id = "id",
            date = "2020-01-01",
            distance = 233,
            litre = 233,
            expense = 233,
            lat = 6.123123132,
            long = -120.123123
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.status, actualResult.status)
        }
        verify(repository).updateActivity(
            token = "token",
            id = "id",
            date = "2020-01-01",
            distance = 233,
            litre = 233,
            expense = 233,
            lat = 6.123123132,
            long = -120.123123
        )
    }

    @Test
    fun `get gas station and success`() = runTest {
        val expectedResult = dummy.gasStations()
        val coveredExpectedResult = flow {
            emit(Resource.Success(expectedResult))
        }
        `when`(
            repository.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        interactor.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertTrue(actualResult is Resource.Success)
            assertEquals(expectedResult, (actualResult as Resource.Success).data)
            assertEquals(expectedResult[0].id, actualResult.data?.get(0)?.id)
        }
        verify(repository).getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
    }

    @Test
    fun `get gas station and loading`() = runTest {
        val expectedResult = dummy.gasStations()
        val coveredExpectedResult = flow {
            emit(Resource.Loading(expectedResult))
        }
        `when`(
            repository.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        interactor.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertTrue(actualResult is Resource.Loading)
            assertEquals(expectedResult, (actualResult as Resource.Loading).data)
            assertEquals(expectedResult[0].id, actualResult.data?.get(0)?.id)
        }
        verify(repository).getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
    }

    @Test
    fun `get gas station and error`() = runTest {
        val expectedResult = "Error"
        val coveredExpectedResult = flow {
            emit(Resource.Error<List<GasStations>>(expectedResult))
        }
        `when`(
            repository.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        interactor.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertTrue(actualResult is Resource.Error)
            assertEquals(expectedResult, (actualResult as Resource.Error).message)
        }
        verify(repository).getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
    }

    @Test
    fun `get local stations and success`() = runTest {
        val expectedResult = dummy.gasStations()
        `when`(
            repository.getLocalStations()
        ).thenReturn(flowOf(expectedResult))
        interactor.getLocalStations().collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult[0].id, actualResult[0].id)
        }
        verify(repository).getLocalStations()
    }

    @Test
    fun `get robo and success`() = runTest {
        val expectedResult = dummy.getRobo()
        `when`(
            repository.getRobo(
                "-06-"
            )
        ).thenReturn(flowOf(expectedResult))
        interactor.getRobo(
            "-06-"
        ).collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.liter, actualResult.liter)
        }
        verify(repository).getRobo(
            "-06-"
        )
    }

    @Test
    fun `get notification and success`() = runTest {
        val expectedResult = dummy.notificationsData()
        `when`(
            repository.getNotification()
        ).thenReturn(flowOf(expectedResult))
        interactor.getNotification().collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult[0].notification, actualResult[0].notification)
        }
        verify(repository).getNotification()
    }

    @Test
    fun `get forecast and success`() = runTest {
        val expectedResult = dummy.forecastResponseData()
        `when`(
            repository.getForecast("token")
        ).thenReturn(flowOf(expectedResult))
        interactor.getForecast("token").collect { actualResult ->
            assertNotNull(actualResult)
            assertEquals(expectedResult, actualResult)
            assertEquals(expectedResult.data, actualResult.data)
        }
        verify(repository).getForecast("token")
    }
}