package id.vee.android.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainCoroutineRule
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class GeneralViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var generalViewModel: GeneralViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        generalViewModel = GeneralViewModel(useCase, pref)
    }

    @Test
    fun `when get user data, return user data`() = mainCoroutineRule.runBlockingTest {
        val expectedResult = dummy.getUserData()
        `when`(useCase.getUser()).thenReturn(flowOf(expectedResult))
        generalViewModel.getUserData()
        val actualResult = generalViewModel.userResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
        assertEquals(expectedResult.firstName, actualResult.firstName)
    }

    @Test
    fun `when get token, return token data`() = mainCoroutineRule.runBlockingTest {
        val expectedResult = dummy.getTokenData()
        `when`(useCase.getToken()).thenReturn(flowOf(expectedResult))
        generalViewModel.getToken()
        val actualResult = generalViewModel.tokenResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult, actualResult)
        assertEquals(expectedResult.accessToken, actualResult.accessToken)
    }

    @Test
    fun `when get theme setting, return theme setting`() = mainCoroutineRule.runBlockingTest {
        val expectedResult = dummy.getSettings()
        `when`(pref.getSettings()).thenReturn(flowOf(expectedResult))
        generalViewModel.getThemeSettings()
        val actualResult = generalViewModel.themeResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expectedResult.theme, actualResult)
    }

    @Test
    fun `when get battery saver setting, return battery saver setting`() =
        mainCoroutineRule.runBlockingTest {
            val expectedResult = dummy.getSettings()
            `when`(pref.getSettings()).thenReturn(flowOf(expectedResult))
            generalViewModel.getBatterySaverSettings()
            val actualResult = generalViewModel.batterySaverResponse.getOrAwaitValue()
            assertNotNull(actualResult)
            assertEquals(expectedResult.saverMode, actualResult)
        }
}