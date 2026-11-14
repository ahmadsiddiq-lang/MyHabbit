package com.myhabbit.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.usecase.history.GetHistoryUseCase
import com.myhabbit.app.domain.usecase.history.HistoryEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

enum class HistoryFilter {
    THIS_WEEK,
    THIS_MONTH,
    ALL
}

data class HistoryUiState(
    val isLoading: Boolean = true,
    val filter: HistoryFilter = HistoryFilter.THIS_WEEK,
    val entries: List<HistoryEntry> = emptyList()
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val filter = MutableStateFlow(HistoryFilter.THIS_WEEK)

    val uiState: StateFlow<HistoryUiState> = combine(
        getHistoryUseCase.observe(),
        filter
    ) { allEntries, currentFilter ->
        val filtered = when (currentFilter) {
            HistoryFilter.THIS_WEEK -> allEntries.filter { isThisWeek(it.periodKey) }
            HistoryFilter.THIS_MONTH -> allEntries.filter { isThisMonth(it.periodKey) }
            HistoryFilter.ALL -> allEntries
        }
        HistoryUiState(
            isLoading = false,
            filter = currentFilter,
            entries = filtered
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(isLoading = true)
    )

    fun onFilterChange(filter: HistoryFilter) {
        this.filter.value = filter
    }

    private fun isThisWeek(periodKey: String): Boolean {
        return when {
            periodKey.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> {
                // Daily: dalam rentang minggu ISO saat ini
                val date = LocalDate.parse(periodKey)
                val today = LocalDate.now()
                val startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                val endOfWeek = startOfWeek.plusDays(6)
                !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)
            }
            periodKey.matches(Regex("\\d{4}-W\\d{2}")) -> {
                // Weekly: minggu saat ini
                val week = periodKey.substring(6, 8).toInt()
                val currentWeek = LocalDate.now().get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                val currentYear = LocalDate.now().get(java.time.temporal.IsoFields.WEEK_BASED_YEAR)
                val year = periodKey.substring(0, 4).toInt()
                week == currentWeek && year == currentYear
            }
            periodKey.matches(Regex("\\d{4}-\\d{2}")) -> {
                // Monthly: tampil hanya di filter This Month / All (periode bulanan melebihi satu minggu)
                false
            }
            else -> false
        }
    }

    private fun isThisMonth(periodKey: String): Boolean {
        val today = LocalDate.now()
        val currentMonth = "%d-%02d".format(today.year, today.monthValue)
        return when {
            periodKey.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> periodKey.startsWith(currentMonth)
            periodKey.matches(Regex("\\d{4}-W\\d{2}")) -> {
                // Minggu dalam bulan ini: minggu ISO yang beririsan dengan bulan ini
                val year = periodKey.substring(0, 4).toInt()
                val week = periodKey.substring(6, 8).toInt()
                val jan4 = LocalDate.of(year, java.time.Month.JANUARY, 4)
                val firstMonday = jan4.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                val weekStart = firstMonday.plusWeeks((week - 1).toLong())
                val weekEnd = weekStart.plusDays(6)
                val monthStart = today.withDayOfMonth(1)
                val monthEnd = today.with(TemporalAdjusters.lastDayOfMonth())
                !weekEnd.isBefore(monthStart) && !weekStart.isAfter(monthEnd)
            }
            periodKey.matches(Regex("\\d{4}-\\d{2}")) -> periodKey == currentMonth
            else -> false
        }
    }
}
