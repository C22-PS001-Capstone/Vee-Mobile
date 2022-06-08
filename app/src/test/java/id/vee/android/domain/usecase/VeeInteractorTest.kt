package id.vee.android.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.VeeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    private lateinit var repository: VeeRepository
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
}