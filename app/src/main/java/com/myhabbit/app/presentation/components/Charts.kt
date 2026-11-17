package com.myhabbit.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.ui.theme.SuccessGreen
import com.myhabbit.app.ui.theme.SuccessGreenDark

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
 * Donut chart status (PRD.md §28.2) via drawArc, dengan persentase
 * di tengah (mockup UI.png #6) dan legenda Selesai/Gagal di kanan.
 */
@Composable
fun DonutChart(
    segments: List<DonutSegment>,
    centerLabel: String? = null,
    modifier: Modifier = Modifier
) {
    val total = segments.sumOf { it.count }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(150.dp)) {
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
            if (centerLabel != null) {
                Text(
                    text = centerLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            segments
                .filter { it.status != HabitStatus.PENDING }
                .forEach { segment ->
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
                            text = segmentStatusLabel(segment.status),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
        }
    }
}

private fun segmentStatusLabel(status: HabitStatus): String = when (status) {
    HabitStatus.COMPLETED -> "Selesai"
    HabitStatus.FAILED -> "Gagal"
    HabitStatus.PENDING -> "Belum dinilai"
}

data class LineChartData(
    val label: String,
    val percentage: Double
)

/**
 * Line chart tren (mockup UI.png #7 "Tren 7 Hari Terakhir") via Canvas:
 * garis + titik hijau, grid horizontal 0..100%. Label sumbu digambar
 * Compose Text (bukan Canvas) agar aksesibel & konsisten tipografinya.
 */
@Composable
fun LineChart(
    data: List<LineChartData>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val lineColor = if (isDark) SuccessGreenDark else SuccessGreen
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .height(160.dp)
                    .width(30.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("100%", "75%", "50%", "25%", "0%").forEach { tick ->
                    Text(
                        text = tick,
                        style = MaterialTheme.typography.labelSmall,
                        color = labelColor
                    )
                }
            }
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
            ) {
                // Grid horizontal 0..100 (5 garis)
                for (i in 0..4) {
                    val y = size.height * i / 4f
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                // Garis tren + titik
                val stepX = if (data.size > 1) size.width / (data.size - 1) else 0f
                val points = data.mapIndexed { index, point ->
                    val x = stepX * index
                    val y = size.height * (1f - (point.percentage.coerceIn(0.0, 100.0) / 100.0).toFloat())
                    Offset(x, y)
                }
                if (points.size > 1) {
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = lineColor,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
                points.forEach { point ->
                    drawCircle(color = lineColor, radius = 4.dp.toPx(), center = point)
                }
            }
        }
        // Label sumbu-x
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp)
        ) {
            data.forEach { point ->
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
