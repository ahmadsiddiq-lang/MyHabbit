package com.myhabbit.app.presentation.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.myhabbit.app.ui.common.AppTopBar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.model.Summary
import com.myhabbit.app.presentation.components.BarChart
import com.myhabbit.app.presentation.components.BarChartData
import com.myhabbit.app.presentation.components.DonutChart
import com.myhabbit.app.presentation.components.DonutSegment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.onFilterChange(filter) },
                        label = { Text(filter.label) }
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

            val summary = state.summary
            if (summary != null) {
                CompletionRateCard(summary)
                CountsCard(summary)
                if (state.chart.bars.isNotEmpty()) {
                    ChartsCard(
                        title = "Completion per Activity",
                        content = {
                            BarChart(
                                data = state.chart.bars.map { bar ->
                                    BarChartData(label = bar.label, percentage = bar.percentage)
                                }
                            )
                        }
                    )
                }
                if (summary.evaluated > 0 || summary.pending > 0) {
                    ChartsCard(
                        title = "Status Distribution",
                        content = {
                            DonutChart(
                                segments = listOf(
                                    DonutSegment(
                                        status = HabitStatus.COMPLETED,
                                        count = state.chart.segments.first { it.label == "Completed" }.count,
                                        color = Color(0xFF2E7D32)
                                    ),
                                    DonutSegment(
                                        status = HabitStatus.FAILED,
                                        count = state.chart.segments.first { it.label == "Failed" }.count,
                                        color = MaterialTheme.colorScheme.error
                                    ),
                                    DonutSegment(
                                        status = HabitStatus.PENDING,
                                        count = state.chart.segments.first { it.label == "Pending" }.count,
                                        color = Color(0xFF9E9E9E)
                                    )
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CompletionRateCard(summary: Summary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completion Rate",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "${summary.completionRate.toInt()}%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${summary.completed} selesai dari ${summary.evaluated} dinilai",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CountsCard(summary: Summary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryRow("Total Records", summary.total.toString())
            SummaryRow("Completed", summary.completed.toString())
            SummaryRow("Failed", summary.failed.toString())
            SummaryRow("Pending", summary.pending.toString())
            SummaryRow("Evaluated", summary.evaluated.toString())
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(text = value)
    }
}

@Composable
private fun ChartsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
            content()
        }
    }
}
