package com.myhabbit.app.presentation.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myhabbit.app.ui.common.AppTopBar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.model.Summary
import com.myhabbit.app.presentation.components.DonutChart
import com.myhabbit.app.presentation.components.DonutSegment
import com.myhabbit.app.presentation.components.LineChart
import com.myhabbit.app.presentation.components.LineChartData
import com.myhabbit.app.presentation.components.StatusIcon
import com.myhabbit.app.ui.theme.FailureRed
import com.myhabbit.app.ui.theme.SuccessGreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Muat ulang setiap kali layar dibuka (bottom nav) agar data tidak stale
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            AppTopBar(title = { Text("Ringkasan") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Toggle segmented "Daftar | Chart" (mockup UI.png #6)
            SummaryModeToggle(
                selected = state.viewMode,
                onSelect = viewModel::onViewModeChange
            )

            // Pill kategori Harian/Mingguan/Bulanan (mockup UI.png #6)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    SummaryFilter.DAILY to "Harian",
                    SummaryFilter.WEEKLY to "Mingguan",
                    SummaryFilter.MONTHLY to "Bulanan"
                ).forEach { (filter, label) ->
                    SummaryCategoryTab(
                        label = label,
                        selected = state.filter == filter,
                        onClick = { viewModel.onFilterChange(filter) }
                    )
                }
            }

            if (state.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            val summary = state.summary ?: return@Column

            when (state.viewMode) {
                SummaryViewMode.LIST -> ListContent(
                    filter = state.filter,
                    summary = summary,
                    segments = state.chart.segments
                )
                SummaryViewMode.CHART -> ChartContent(
                    trend = state.trend,
                    summary = summary
                )
            }
        }
    }
}

/** Segmented toggle Daftar/Chart: container lavender, segmen terpilih putih. */
@Composable
private fun SummaryModeToggle(
    selected: SummaryViewMode,
    onSelect: (SummaryViewMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)
    ) {
        SummaryViewMode.entries.forEach { mode ->
            val isSelected = mode == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                    .clickable { onSelect(mode) }
                    .padding(vertical = 10.dp)
                    .semantics {
                        contentDescription = "Mode ${mode.label}"
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/** Pill kategori: terisi ungu saat dipilih, outline saat tidak (ala Beranda). */
@Composable
private fun SummaryCategoryTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(50.dp)
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .semantics { contentDescription = "Kategori $label" }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

/**
 * Tab DAFTAR (mockup UI.png #6): judul per kategori, baris statistik
 * Selesai/Gagal/Total/Rata-rata, lalu donut chart dengan persen di tengah.
 */
@Composable
private fun ListContent(
    filter: SummaryFilter,
    summary: Summary,
    segments: List<SegmentUi>
) {
    val categoryLabel = when (filter) {
        SummaryFilter.DAILY -> "Harian"
        SummaryFilter.WEEKLY -> "Mingguan"
        SummaryFilter.MONTHLY -> "Bulanan"
        SummaryFilter.ALL -> "Semua"
    }
    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID")))

    Text(
        text = "Ringkasan $categoryLabel ($today)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            StatRow(
                status = HabitStatus.COMPLETED,
                label = "Selesai",
                value = summary.completed.toString()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            StatRow(
                status = HabitStatus.FAILED,
                label = "Gagal",
                value = summary.failed.toString()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            StatRow(
                status = HabitStatus.PENDING,
                label = "Total",
                value = summary.total.toString()
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            StatRow(
                status = null,
                label = "Rata-rata",
                value = "${summary.completionRate.toInt()}%"
            )
        }
    }

    DonutChart(
        segments = listOf(
            DonutSegment(
                status = HabitStatus.COMPLETED,
                count = segments.first { it.label == "Completed" }.count,
                color = SuccessGreen
            ),
            DonutSegment(
                status = HabitStatus.FAILED,
                count = segments.first { it.label == "Failed" }.count,
                color = FailureRed
            ),
            DonutSegment(
                status = HabitStatus.PENDING,
                count = segments.first { it.label == "Pending" }.count,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ),
        centerLabel = "${summary.completionRate.toInt()}%",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
private fun StatRow(
    status: HabitStatus?,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .semantics { contentDescription = "$label $value" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status != null) {
            StatusIcon(status = status, size = 22.dp)
        } else {
            // Ikon rata-rata: cincin kecil ala donut (mockup ⊕)
            RingIcon()
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Cincin kecil untuk baris Rata-rata (mockup UI.png #6 ikon ⊕). */
@Composable
private fun RingIcon() {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(22.dp)) {
        drawCircle(
            color = androidx.compose.ui.graphics.Color(0xFF6A5AE0),
            radius = size.minDimension / 2,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
        )
    }
}

/**
 * Tab CHART (mockup UI.png #7): "Tren 7 Hari Terakhir" line chart
 * + kartu Rata-rata di bawah.
 */
@Composable
private fun ChartContent(
    trend: List<TrendUi>,
    summary: Summary
) {
    Text(
        text = "Tren 7 Hari Terakhir",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Card(modifier = Modifier.fillMaxWidth()) {
        LineChart(
            data = trend.map { LineChartData(label = it.label, percentage = it.percentage) },
            modifier = Modifier.padding(16.dp)
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MiniBarsIcon()
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = "Rata-rata",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${summary.completionRate.toInt()}%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** Ikon bar mini hijau (mockup UI.png #7 kartu Rata-rata). */
@Composable
private fun MiniBarsIcon() {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(36.dp)) {
        val barWidth = size.width / 5
        val heights = listOf(0.35f, 0.6f, 0.85f, 1f)
        heights.forEachIndexed { index, fraction ->
            val h = size.height * fraction
            drawRect(
                color = SuccessGreen,
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = barWidth * (index + 0.7f),
                    y = size.height - h
                ),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.6f, h)
            )
        }
    }
}
