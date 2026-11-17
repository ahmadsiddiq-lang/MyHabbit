package com.myhabbit.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitStatus

/**
 * Popup "Pilih Status" (mockup UI.png layar #5): bottom sheet dengan
 * opsi Selesai / Tidak Dilakukan / Belum dinilai + Batal.
 * Menggantikan 3 tombol inline ○ ✓ ✕ di kartu (PRD.md §10).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusPickerSheet(
    onStatusSelected: (HabitStatus) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Pilih Status",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            StatusOption(
                status = HabitStatus.COMPLETED,
                title = "Selesai",
                subtitle = "Tandai sebagai selesai",
                onClick = { onStatusSelected(HabitStatus.COMPLETED) }
            )
            StatusOption(
                status = HabitStatus.FAILED,
                title = "Tidak Dilakukan",
                subtitle = "Tandai sebagai gagal",
                onClick = { onStatusSelected(HabitStatus.FAILED) }
            )
            StatusOption(
                status = HabitStatus.PENDING,
                title = "Belum dinilai",
                subtitle = "Kembalikan ke belum dinilai",
                onClick = { onStatusSelected(HabitStatus.PENDING) }
            )

            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Batal")
            }
        }
    }
}

@Composable
private fun StatusOption(
    status: HabitStatus,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .semantics { contentDescription = "$title — $subtitle" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusIcon(status = status, size = 30.dp)
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
