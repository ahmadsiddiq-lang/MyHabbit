package com.myhabbit.app.presentation.dashboard

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.service.PeriodCalculatorImpl
import com.myhabbit.app.domain.usecase.habit.DeleteHabitUseCase
import com.myhabbit.app.domain.usecase.habit.GetActiveHabitsUseCase
import com.myhabbit.app.domain.usecase.record.GetCurrentPeriodStatusesUseCase
import com.myhabbit.app.domain.usecase.record.UpdateHabitStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DashboardViewModelTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val today = LocalDate.of(2026, 11, 13)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val habitRepository = HabitRepositoryImpl(database.habitDao())
        val recordRepository = HabitRecordRepositoryImpl(database.habitRecordDao())
        val calculator = PeriodCalculatorImpl()

        viewModel = DashboardViewModel(
            getActiveHabitsUseCase = GetActiveHabitsUseCase(habitRepository),
            getCurrentPeriodStatusesUseCase =
                GetCurrentPeriodStatusesUseCase(recordRepository, calculator),
            updateHabitStatusUseCase = UpdateHabitStatusUseCase(recordRepository, calculator),
            deleteHabitUseCase = DeleteHabitUseCase(habitRepository)
        )
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    private suspend fun seed(
        id: String,
        category: HabitCategory
    ) {
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit(
                id = id,
                title = "Habit $id",
                category = category,
                description = null,
                isActive = true,
                createdAt = 1L,
                updatedAt = 1L,
                deletedAt = null
            )
        )
    }

    @Test
    fun emptyState_allSectionsEmpty() = runTest(testDispatcher) {
        val state = viewModel.uiState.first { !it.isLoading }

        assertTrue(state.daily.isEmpty())
        assertTrue(state.weekly.isEmpty())
        assertTrue(state.monthly.isEmpty())
    }

    @Test
    fun state_groupsByCategory_withPendingDefaults() = runTest(testDispatcher) {
        seed("h1", HabitCategory.DAILY)
        seed("h2", HabitCategory.WEEKLY)
        seed("h3", HabitCategory.MONTHLY)

        val state = viewModel.uiState.first {
            !it.isLoading && it.daily.isNotEmpty() && it.weekly.isNotEmpty() && it.monthly.isNotEmpty()
        }

        assertEquals(1, state.daily.size)
        assertEquals(1, state.weekly.size)
        assertEquals(1, state.monthly.size)
        assertEquals(HabitStatus.PENDING, state.daily.first().status)
        assertEquals(HabitStatus.PENDING, state.weekly.first().status)
        assertEquals(HabitStatus.PENDING, state.monthly.first().status)
    }

    @Test
    fun onStatusChange_updatesRecord_uiReflectsChange() = runTest(testDispatcher) {
        seed("h1", HabitCategory.DAILY)

        val initial = viewModel.uiState.first { !it.isLoading && it.daily.isNotEmpty() }
        val item = initial.daily.first()
        viewModel.onStatusChange(item, HabitStatus.COMPLETED)
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.uiState.first { it.daily.first().status == HabitStatus.COMPLETED }
        assertEquals(HabitStatus.COMPLETED, updated.daily.first().status)
    }

    @Test
    fun progressCounts_reflectStatuses() = runTest(testDispatcher) {
        seed("h1", HabitCategory.DAILY)
        seed("h2", HabitCategory.DAILY)
        seed("h3", HabitCategory.DAILY)

        val state = viewModel.uiState.first {
            !it.isLoading && it.daily.size == 3
        }

        val daily = state.daily
        val completed = daily.count { it.status == HabitStatus.COMPLETED }
        val pending = daily.count { it.status == HabitStatus.PENDING }

        assertEquals(3, completed + pending)
    }

    @Test
    fun deleteHabit_removesItemFromDashboard() = runTest(testDispatcher) {
        seed("h1", HabitCategory.DAILY)
        seed("h2", HabitCategory.DAILY)

        val initial = viewModel.uiState.first { !it.isLoading && it.daily.size == 2 }
        val target = initial.daily.first { it.habit.id == "h1" }

        viewModel.deleteHabit(target.habit.id)
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.uiState.first { it.daily.size == 1 }
        assertEquals("h2", updated.daily.first().habit.id)
        assertNull(updated.error)
    }

    @Test
    fun onActivityAdded_showsNewActivityImmediately() = runTest(testDispatcher) {
        val initial = viewModel.uiState.first { !it.isLoading }
        assertTrue(initial.daily.isEmpty())

        seed("h1", HabitCategory.DAILY)
        viewModel.onActivityAdded()
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = viewModel.uiState.first { it.daily.isNotEmpty() }
        assertEquals("h1", updated.daily.first().habit.id)
    }
}
