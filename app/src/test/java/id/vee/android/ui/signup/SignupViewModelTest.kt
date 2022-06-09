package id.vee.android.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class SignupViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var signupViewModel: SignupViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        signupViewModel = SignupViewModel(useCase)
    }

    @Test
    fun `test sign up and success`() = runTest {
        val expectedSignup = dummy.getBasicResponse()
        `when`(
            useCase.signup(
                firstName = "firstName",
                lastName = "lastName",
                email = "email@mail.com",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedSignup))
        signupViewModel.signup(
            firstName = "firstName",
            lastName = "lastName",
            email = "email@mail.com",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
        val actualSignup = signupViewModel.response.getOrAwaitValue()
        assertNotNull(actualSignup)
        assertEquals(expectedSignup, actualSignup)
        assertEquals(expectedSignup.status, actualSignup.status)
    }
}