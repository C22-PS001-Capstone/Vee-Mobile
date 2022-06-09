package id.vee.android.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.Resource
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(useCase, pref)
    }

    /*@Test
    fun `get robo and success`() = runTest {
        val expectedResult = dummy.getRobo()
        `when`(
            useCase.getRobo(
                "-06-"
            )
        ).thenReturn(flowOf(expectedResult))
        homeViewModel.getRobo(
            "-06-"
        )
        val actualResult = homeViewModel.roboResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.liter, actualResult.liter)
    }*/

    @Test
    fun `get gas station and success`() = runTest {
        val expectedResult = dummy.gasStations()
        val coveredExpectedResult = flow {
            emit(Resource.Success(expectedResult))
        }
        `when`(
            useCase.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        homeViewModel.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
        val actualResult = homeViewModel.gasStationsResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Resource.Success)
        Assert.assertEquals(expectedResult, (actualResult as Resource.Success).data)
        Assert.assertEquals(expectedResult[0].id, actualResult.data?.get(0)?.id)
    }

    @Test
    fun `get gas station and loading`() = runTest {
        val expectedResult = dummy.gasStations()
        val coveredExpectedResult = flow {
            emit(Resource.Loading(expectedResult))
        }
        `when`(
            useCase.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        homeViewModel.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
        val actualResult = homeViewModel.gasStationsResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Resource.Loading)
        Assert.assertEquals(expectedResult, (actualResult as Resource.Loading).data)
        Assert.assertEquals(expectedResult[0].id, actualResult.data?.get(0)?.id)
    }

    @Test
    fun `get gas station and error`() = runTest {
        val expectedResult = "Error"
        val coveredExpectedResult = flow {
            emit(Resource.Error<List<GasStations>>(expectedResult))
        }
        `when`(
            useCase.getGasStations(
                token = "token",
                lat = 6.123123132,
                lon = -120.123123
            )
        ).thenReturn(coveredExpectedResult)
        homeViewModel.getGasStations(
            token = "token",
            lat = 6.123123132,
            lon = -120.123123
        )
        val actualResult = homeViewModel.gasStationsResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertTrue(actualResult is Resource.Error)
        Assert.assertEquals(expectedResult, (actualResult as Resource.Error).message)
    }


    @Test
    fun `get forecast and success`() = runTest {
        val expectedResult = dummy.forecastResponseData()
        `when`(
            useCase.getForecast("token")
        ).thenReturn(flowOf(expectedResult))
        homeViewModel.getForecast("token")
        val actualResult = homeViewModel.forecastResponse.getOrAwaitValue()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
        Assert.assertEquals(expectedResult.data, actualResult.data)
    }
}