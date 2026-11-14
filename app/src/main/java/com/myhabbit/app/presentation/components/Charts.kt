package com.myhabbit.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitStatus

data class BarChartData(
    val label: String,
    val percentage: Double
)

/**
 * Horizontal bar chart sederhana via Canvas (PRD.md §28.1).
 * Data disiapkan ViewModel; komponen hanya menggambar.
 */
@Composable
fun BarChart(
    data: List<BarChartData>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        data.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(0.35f)
                        .padding(end = 8.dp)
                )
                BarTrack(
                    percentage = item.percentage,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = "${item.percentage.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun BarTrack(
    percentage: Double,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val barColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.height(14.dp)) {
        val cornerRadius = size.height / 2
        // Track
        drawRoundRect(
            color = trackColor,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
        )
        // Bar
        val filled = (percentage.coerceIn(0.0, 100.0) / 100.0).toFloat() * size.width
        if (filled > 0f) {
            drawRoundRect(
                color = barColor,
                size = Size(filled, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

data class DonutSegment(
    val status: HabitStatus,
    val count: Int,
    val color: Color
)

/**
 * Donut chart status (PRD.md §28.2) via drawArc.
 */
@Composable
fun DonutChart(
    segments: List<DonutSegment>,
    modifier: Modifier = Modifier
) {
    val total = segments.sumOf { it.count }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = modifier.size(140.dp)) {
            if (total == 0) return@Canvas
            val stroke = size.minDimension / 5
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)
            var startAngle = -90f
            segments.forEach { segment ->
                if (segment.count > 0) {
                    val sweep = segment.count.toFloat() / total * 360f
                    drawArc(
                        color = segment.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
                    )
                    startAngle += sweep
                }
            }
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            segments.forEach { segment ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Surface(
                        color = segment.color,
                        shape = RoundedCornerShape(2.dp),
                        modifier = Modifier.size(12.dp)
                    ) {}
                    Text(
                        text = "${segmentStatusLabel(segment.status)}: ${segment.count}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

private fun segmentStatusLabel(status: HabitStatus): String = when (status) {
    HabitStatus.COMPLETED -> "Completed"
    HabitStatus.FAILED -> "Failed"
    HabitStatus.PENDING -> "Pending"
}
