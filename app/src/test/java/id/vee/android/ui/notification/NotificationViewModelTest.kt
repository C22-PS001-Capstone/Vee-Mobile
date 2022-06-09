package id.vee.android.ui.notification

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
internal class NotificationViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dummy = DataDummy

    @Mock
    private lateinit var useCase: VeeUseCase
    private lateinit var notificationViewModel: NotificationViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        notificationViewModel = NotificationViewModel(useCase)
    }

    @Test
    fun `when get notification then return notification data`() = runTest {
        val expected = dummy.notificationsData()
        `when`(useCase.getNotification()).thenReturn(flowOf(expected))
        notificationViewModel.getNotification()
        val actualResult = notificationViewModel.notificationResponse.getOrAwaitValue()
        assertNotNull(actualResult)
        assertEquals(expected, actualResult)
        assertEquals(expected.size, actualResult.size)
        assertEquals(expected[0].id, actualResult[0].id)
    }
}