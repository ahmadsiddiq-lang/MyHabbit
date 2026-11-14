package com.myhabbit.app.presentation.history

import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.usecase.history.GetHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var habitDao: HabitDao
    private lateinit var habitRecordDao: HabitRecordDao

    private val habitsFlow = MutableStateFlow<List<HabitEntity>>(emptyList())
    private val recordsFlow = MutableStateFlow<List<HabitRecordEntity>>(emptyList())

    private val today = LocalDate.now()

    private fun currentWeekKey(): String {
        val week = today.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
        val year = today.get(java.time.temporal.IsoFields.WEEK_BASED_YEAR)
        return "%d-W%02d".format(year, week)
    }

    private fun currentMonthKey(): String = "%d-%02d".format(today.year, today.monthValue)

    private fun previousMonthKey(): String {
        val prev = today.minusMonths(1)
        return "%d-%02d".format(prev.year, prev.monthValue)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        habitDao = Mockito.mock(HabitDao::class.java)
        habitRecordDao = Mockito.mock(HabitRecordDao::class.java)

        whenever(habitDao.getAllHabitsAsFlow()).thenReturn(habitsFlow as Flow<List<HabitEntity>>)
        whenever(habitRecordDao.observeAllRecords()).thenReturn(recordsFlow as Flow<List<HabitRecordEntity>>)

        val thisMonthPrefix = currentMonthKey()

        habitsFlow.value = listOf(
            habit("h1", "Belajar", "DAILY"),
            habit("h2", "Lari", "WEEKLY"),
            habit("h3", "Membaca", "MONTHLY"),
            habit("h4", "Olahraga", "MONTHLY"),
            habit("h5", "Python", "DAILY")
        )
        recordsFlow.value = listOf(
            record("h1", "DAILY", today.toString(), "COMPLETED"),
            record("h2", "WEEKLY", currentWeekKey(), "PENDING"),
            record("h3", "MONTHLY", currentMonthKey(), "COMPLETED"),
            record("h4", "MONTHLY", previousMonthKey(), "FAILED"),
            record("h5", "DAILY", today.minusMonths(2).toString(), "COMPLETED")
        )
    }

    private fun habit(id: String, title: String, category: String) = HabitEntity(
        id = id,
        title = title,
        category = category,
        description = null,
        isActive = true,
        createdAt = 1L,
        updatedAt = 1L,
        deletedAt = null
    )

    private fun record(habitId: String, periodType: String, periodKey: String, status: String) =
        HabitRecordEntity(
            id = "rec-$habitId-$periodKey",
            habitId = habitId,
            periodType = periodType,
            periodKey = periodKey,
            status = status,
            createdAt = 1L,
            updatedAt = 1L
        )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun filterThisWeek_includesDailyTodayAndWeeklyCurrent() = runTest(testDispatcher) {
        val viewModel = HistoryViewModel(GetHistoryUseCase(habitDao, habitRecordDao))
        viewModel.onFilterChange(HistoryFilter.THIS_WEEK)

        val state = viewModel.uiState.first { !it.isLoading && it.filter == HistoryFilter.THIS_WEEK }

        assertEquals(2, state.entries.size)
        assertTrue(state.entries.all {
            it.periodKey == today.toString() || it.periodKey == currentWeekKey()
        })
    }

    @Test
    fun filterThisMonth_excludesPreviousMonth() = runTest(testDispatcher) {
        val viewModel = HistoryViewModel(GetHistoryUseCase(habitDao, habitRecordDao))
        viewModel.onFilterChange(HistoryFilter.THIS_MONTH)

        val state = viewModel.uiState.first {
            !it.isLoading && it.filter == HistoryFilter.THIS_MONTH
        }

        assertEquals(3, state.entries.size)
        assertTrue(state.entries.none { it.periodKey == previousMonthKey() })
    }

    @Test
    fun filterAll_includesEverything() = runTest(testDispatcher) {
        val viewModel = HistoryViewModel(GetHistoryUseCase(habitDao, habitRecordDao))
        viewModel.onFilterChange(HistoryFilter.ALL)

        val state = viewModel.uiState.first {
            !it.isLoading && it.filter == HistoryFilter.ALL
        }

        assertEquals(5, state.entries.size)
    }

    @Test
    fun entries_showCorrectStatus() = runTest(testDispatcher) {
        val viewModel = HistoryViewModel(GetHistoryUseCase(habitDao, habitRecordDao))
        viewModel.onFilterChange(HistoryFilter.ALL)

        val state = viewModel.uiState.first {
            !it.isLoading && it.filter == HistoryFilter.ALL
        }

        assertEquals(HabitStatus.FAILED, state.entries.first { it.habitId == "h4" }.status)
        assertEquals(HabitStatus.PENDING, state.entries.first { it.habitId == "h2" }.status)
    }
}
