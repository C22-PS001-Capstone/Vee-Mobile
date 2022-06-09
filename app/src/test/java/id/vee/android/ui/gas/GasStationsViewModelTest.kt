package id.vee.android.ui.gas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.vee.android.DataDummy
import id.vee.android.MainDispatcherRule
import id.vee.android.data.Resource
import id.vee.android.data.local.SettingsPreferences
import id.vee.android.domain.model.GasStations
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
internal class GasStationsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var pref: SettingsPreferences
    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var gasStationsViewModel: GasStationsViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        gasStationsViewModel = GasStationsViewModel(useCase, pref)
    }

    @Test
    fun `when get gas stations return gas stations data`() {
        val expectedResult = dummy.gasStations()
        val coveredExpected: Flow<Resource<List<GasStations>>> = flow {
            emit(Resource.Success(expectedResult))
        }
        `when`(
            useCase.getGasStations(
                "user-token",
                6.38383,
                -120.33231
            )
        ).thenReturn(coveredExpected)
        gasStationsViewModel.getGasStations(
            "user-token",
            6.38383,
            -120.33231
        )
        val actualResult = gasStationsViewModel.gasStationsResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Success)
        (actualResult as Resource.Success).data?.let {
            assertEquals(expectedResult, it)
        }
    }
}