package id.vee.android.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.Resource
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
import id.vee.android.ui.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class ProfileViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var profileViewModel: ProfileViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        profileViewModel = ProfileViewModel(useCase, pref)
    }

    @Test
    fun `get user detail and success`() = runTest {
        val expectedResult = dummy.getUserDetail()
        val dummyToken = dummy.getTokenData()
        `when`(useCase.userDetail(dummyToken)).thenReturn(flowOf(expectedResult))
        profileViewModel.getUserData()
        val actualResult = profileViewModel.responseDetail.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `save user and success`() = runTest {
        val dummyData = dummy.getUserData()
        profileViewModel.saveUser(dummyData)
        verify(useCase).saveUser(dummyData)
    }

    @Test
    fun `delete user and success`() = runTest {
        val dummyToken = dummy.getTokenData().refreshToken
        profileViewModel.logout(dummyToken)
        verify(useCase).deleteUser()
        verify(useCase).deleteToken()
    }

    @Test
    fun `update name and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            useCase.updateName(
                token = "token",
                firstName = "firstName",
                lastName = "lastName"
            )
        ).thenReturn(flowOf(expectedResult))
        profileViewModel.updateName(
            token = "token",
            firstName = "firstName",
            lastName = "lastName"
        )
        val actualResult = profileViewModel.updateNameResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `update password and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            useCase.updatePassword(
                token = "token",
                passwordCurrent = "password",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedResult))
        profileViewModel.updatePassword(
            token = "token",
            passwordCurrent = "password",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
        val actualResult = profileViewModel.updatePasswordResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `add password and success`() = runTest {
        val expectedResult = dummy.getBasicResponse()
        `when`(
            useCase.addPassword(
                token = "token",
                password = "password",
                passwordConfirm = "passwordConfirm"
            )
        ).thenReturn(flowOf(expectedResult))
        profileViewModel.addPassword(
            token = "token",
            password = "password",
            passwordConfirm = "passwordConfirm"
        )
        val actualResult = profileViewModel.addPasswordResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.status, actualResult.status)
    }

    @Test
    fun `testing save theme setting`() = runTest {
        profileViewModel.saveThemeSetting(false)
        verify(pref).saveThemeSetting(false)
    }

    @Test
    fun `testing save battery saver setting`() = runTest {
        profileViewModel.saveBatterySaverSetting(false)
        verify(pref).saveThemeSetting(false)
    }
}