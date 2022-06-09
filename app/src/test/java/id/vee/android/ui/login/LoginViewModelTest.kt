package id.vee.android.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
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
internal class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(useCase)
    }

    @Test
    fun `login and return success`() = runTest {
        val expectedResult = dummy.getLoginResponse()
        `when`(useCase.login("email@mail.com", "password")).thenReturn(flowOf(expectedResult))
        loginViewModel.login("email@mail.com", "password")
        val actualResult = loginViewModel.response.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
        assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `get user detail and return success`() = runTest {
        val expectedResult = dummy.getUserDetail()
        val dummyToken = dummy.getTokenData()
        `when`(useCase.userDetail(dummyToken)).thenReturn(flowOf(expectedResult))
        loginViewModel.userDetail(dummyToken)
        val actualResult = loginViewModel.responseDetail.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
        assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `login google and return success`() = runTest {
        val expectedResult = dummy.getLoginResponse()

        `when`(useCase.loginGoogle("token")).thenReturn(flowOf(expectedResult))
        loginViewModel.loginGoogle("token")
        val actualResult = loginViewModel.response.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
        assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `testing save token`() = runTest {
        val dummyToken = dummy.getTokenData()
        loginViewModel.saveToken(dummyToken)
        verify(useCase).saveToken(dummyToken)
    }

    @Test
    fun `testing save user`() = runTest {
        val dummyUser = dummy.getUserData()
        loginViewModel.saveUser(dummyUser)
        verify(useCase).saveUser(dummyUser)
    }
}