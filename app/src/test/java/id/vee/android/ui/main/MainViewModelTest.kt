package id.vee.android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
internal class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(useCase, pref)
    }

    @Test
    fun `testing update location`() = runTest {
        mainViewModel.updateLocation(-6.2233232, 106.8232323)
        verify(pref).setLocation(-6.2233232, 106.8232323)
    }

    @Test
    fun `get local stations and success`() = runTest {
        val expectedResult = dummy.gasStations()
        `when`(
            useCase.getLocalStations()
        ).thenReturn(flowOf(expectedResult))
        mainViewModel.getLocalStations()
        val actualResult = mainViewModel.stationsResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult[0].id, actualResult[0].id)
    }
}