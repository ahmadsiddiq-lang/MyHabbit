package com.myhabbit.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.usecase.habit.DeleteHabitUseCase
import com.myhabbit.app.domain.usecase.habit.GetActiveHabitsUseCase
import com.myhabbit.app.domain.usecase.record.GetCurrentPeriodStatusesUseCase
import com.myhabbit.app.domain.usecase.record.HabitWithStatus
import com.myhabbit.app.domain.usecase.record.UpdateHabitStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val todayLabel: String = "",
    val daily: List<HabitWithStatus> = emptyList(),
    val weekly: List<HabitWithStatus> = emptyList(),
    val monthly: List<HabitWithStatus> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getActiveHabitsUseCase: GetActiveHabitsUseCase,
    private val getCurrentPeriodStatusesUseCase: GetCurrentPeriodStatusesUseCase,
    private val updateHabitStatusUseCase: UpdateHabitStatusUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {

    private val manualError = MutableStateFlow<String?>(null)
    private val refreshTrigger = MutableStateFlow(0)

    private val today: LocalDate = LocalDate.now()

    val uiState: StateFlow<DashboardUiState> =
        combine(
            getActiveHabitsUseCase(),
            manualError,
            refreshTrigger
        ) { habits, error, _ ->
            val withStatus = getCurrentPeriodStatusesUseCase(habits, today)
            DashboardUiState(
                isLoading = false,
                todayLabel = formatTodayLabel(today),
                daily = withStatus.filter { it.habit.category == HabitCategory.DAILY },
                weekly = withStatus.filter { it.habit.category == HabitCategory.WEEKLY },
                monthly = withStatus.filter { it.habit.category == HabitCategory.MONTHLY },
                error = error
            )
        }
            .catch { e ->
                emit(
                    DashboardUiState(
                        isLoading = false,
                        error = "Gagal memuat data: ${e.message}"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DashboardUiState(isLoading = true)
            )

    fun onStatusChange(habit: HabitWithStatus, status: HabitStatus) {
        viewModelScope.launch {
            when (val result = updateHabitStatusUseCase(habit.habit.id, habit.habit.category, status)) {
                is AppResult.Error -> manualError.value = result.message
                is AppResult.Success -> {
                    manualError.value = null
                    // Trigger re-emission karena Room flow hanya memantau tabel habits
                    refreshTrigger.value = refreshTrigger.value + 1
                }
            }
        }
    }

    /** Hapus activity (soft delete, PRD.md §16) — dipanggil setelah konfirmasi. */
    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            when (val result = deleteHabitUseCase(habitId)) {
                is AppResult.Error -> manualError.value = result.message
                is AppResult.Success -> manualError.value = null
            }
        }
    }

    fun clearError() {
        manualError.value = null
    }

    private fun formatTodayLabel(date: LocalDate): String {
        val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
        return "$dayName, ${date.format(formatter)}"
    }
}
