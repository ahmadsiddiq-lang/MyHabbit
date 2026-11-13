package com.myhabbit.app.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Kartu progress periode (PRD.md §9):
 * mis. "3 / 5" dan "60%".
 *
 * Progress dihitung dari status evaluated (completed + failed)
 * dibanding total activity, sesuai kebutuhan dashboard sederhana.
 */
@Composable
fun ProgressCard(
    label: String,
    completed: Int,
    evaluated: Int,
    total: Int,
    completionRate: Double,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = label)
            Text(text = "$completed / $total")
            Text(text = "${completionRate.toInt()}%")
            LinearProgressIndicator(
                progress = {
                    if (total > 0) evaluated.toFloat() / total else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
