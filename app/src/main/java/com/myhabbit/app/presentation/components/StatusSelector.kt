package com.myhabbit.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitStatus

/**
 * Selector status ○ / ✓ / ✕ (PRD.md §10).
 * Status dibedakan dengan simbol, bukan hanya warna (PROMPT.md §14).
 */
@Composable
fun StatusSelector(
    currentStatus: HabitStatus,
    onStatusSelected: (HabitStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        StatusButton(
            status = HabitStatus.PENDING,
            isSelected = currentStatus == HabitStatus.PENDING,
            icon = {
                // Lingkaran kosong ○ digambar dengan border agar tidak bergantung pada icon extended
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        )
                )
            },
            contentDescription = "Tandai belum dinilai",
            onStatusSelected = onStatusSelected
        )
        StatusButton(
            status = HabitStatus.COMPLETED,
            isSelected = currentStatus == HabitStatus.COMPLETED,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(22.dp)
                )
            },
            contentDescription = "Tandai selesai",
            onStatusSelected = onStatusSelected
        )
        StatusButton(
            status = HabitStatus.FAILED,
            isSelected = currentStatus == HabitStatus.FAILED,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
            },
            contentDescription = "Tandai tidak dilakukan",
            onStatusSelected = onStatusSelected
        )
    }
}

@Composable
private fun StatusButton(
    status: HabitStatus,
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    contentDescription: String,
    onStatusSelected: (HabitStatus) -> Unit
) {
    IconButton(
        onClick = { onStatusSelected(status) },
        modifier = Modifier.semantics { this.contentDescription = contentDescription }
    ) {
        // Background berwarna saat dipilih agar status aktif jelas tanpa hanya mengandalkan warna ikon
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}
