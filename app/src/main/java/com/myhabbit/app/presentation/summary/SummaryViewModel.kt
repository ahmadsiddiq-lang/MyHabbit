package com.myhabbit.app.presentation.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.Summary
import com.myhabbit.app.domain.usecase.summary.GetSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SummaryFilter(val label: String, val category: HabitCategory?) {
    ALL("All", null),
    DAILY("Daily", HabitCategory.DAILY),
    WEEKLY("Weekly", HabitCategory.WEEKLY),
    MONTHLY("Monthly", HabitCategory.MONTHLY)
}

data class SummaryUiState(
    val isLoading: Boolean = true,
    val filter: SummaryFilter = SummaryFilter.ALL,
    val summary: Summary? = null,
    val error: String? = null
)

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getSummaryUseCase: GetSummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun onFilterChange(filter: SummaryFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        load()
    }

    fun refresh() {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                val summary = getSummaryUseCase(_uiState.value.filter.category)
                _uiState.value = _uiState.value.copy(isLoading = false, summary = summary)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal memuat ringkasan: ${e.message}"
                )
            }
        }
    }
}
