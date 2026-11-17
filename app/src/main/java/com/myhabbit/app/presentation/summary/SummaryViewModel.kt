package com.myhabbit.app.presentation.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.Summary
import com.myhabbit.app.domain.usecase.summary.GetHabitStatisticsUseCase
import com.myhabbit.app.domain.usecase.summary.GetSummaryUseCase
import com.myhabbit.app.domain.usecase.summary.GetTrendUseCase
import com.myhabbit.app.domain.usecase.summary.TrendPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Kategori ringkasan — hanya 3 pill (Harian/Mingguan/Bulanan)
 * sesuai mockup UI.png #6. ALL dipertahankan untuk kompatibilitas.
 */
enum class SummaryFilter(val label: String, val category: HabitCategory?) {
    ALL("All", null),
    DAILY("Daily", HabitCategory.DAILY),
    WEEKLY("Weekly", HabitCategory.WEEKLY),
    MONTHLY("Monthly", HabitCategory.MONTHLY)
}

/**
 * Mode tampilan Ringkasan (mockup UI.png #6/#7):
 * Daftar = statistik + donut, Chart = tren 7 periode.
 */
enum class SummaryViewMode(val label: String) {
    LIST("Daftar"),
    CHART("Chart")
}

/**
 * Data chart siap gambar (ARCHITECTURE.md §12.4):
 * ViewModel yang menyiapkan, komponen chart hanya menggambar.
 */
data class ChartUiState(
    val bars: List<BarUi> = emptyList(),
    val segments: List<SegmentUi> = emptyList()
)

data class BarUi(
    val label: String,
    val percentage: Double
)

data class SegmentUi(
    val label: String,
    val count: Int
)

data class TrendUi(
    val label: String,
    val percentage: Double
)

data class SummaryUiState(
    val isLoading: Boolean = true,
    val filter: SummaryFilter = SummaryFilter.DAILY,
    val viewMode: SummaryViewMode = SummaryViewMode.LIST,
    val summary: Summary? = null,
    val chart: ChartUiState = ChartUiState(),
    val trend: List<TrendUi> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getSummaryUseCase: GetSummaryUseCase,
    private val getHabitStatisticsUseCase: GetHabitStatisticsUseCase,
    private val getTrendUseCase: GetTrendUseCase
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

    fun onViewModeChange(mode: SummaryViewMode) {
        _uiState.value = _uiState.value.copy(viewMode = mode)
    }

    fun refresh() {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                val category = _uiState.value.filter.category
                val summary = getSummaryUseCase(category)
                val statistics = getHabitStatisticsUseCase(category)
                val trend: List<TrendPoint> = if (category != null) {
                    getTrendUseCase(category)
                } else {
                    getTrendUseCase(HabitCategory.DAILY)
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    summary = summary,
                    chart = buildChart(statistics, summary),
                    trend = trend.map { TrendUi(label = it.label, percentage = it.percentage) },
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Gagal memuat ringkasan: ${e.message}"
                )
            }
        }
    }

    private fun buildChart(
        statistics: List<com.myhabbit.app.domain.usecase.summary.HabitStatistic>,
        summary: Summary
    ): ChartUiState {
        return ChartUiState(
            bars = statistics.map { stat ->
                BarUi(label = stat.habitTitle, percentage = stat.completionRate)
            },
            segments = listOf(
                SegmentUi(label = "Completed", count = summary.completed),
                SegmentUi(label = "Failed", count = summary.failed),
                SegmentUi(label = "Pending", count = summary.pending)
            )
        )
    }
}
