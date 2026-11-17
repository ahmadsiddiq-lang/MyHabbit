package com.myhabbit.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.ui.theme.SuccessGreen
import com.myhabbit.app.ui.theme.SuccessGreenDark
import com.myhabbit.app.ui.theme.FailureRed
import com.myhabbit.app.ui.theme.FailureRedDark

/**
 * Ikon lingkaran status (mockup UI.png): ✓ hijau, ✕ merah, ○ kosong.
 * Status dibedakan dengan simbol, bukan hanya warna (PROMPT.md §14).
 */
@Composable
fun StatusIcon(
    status: HabitStatus,
    size: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val green = if (isDark) SuccessGreenDark else SuccessGreen
    val red = if (isDark) FailureRedDark else FailureRed

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            HabitStatus.COMPLETED -> FilledCircle(color = green, diameter = size) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.62f)
                )
            }
            HabitStatus.FAILED -> FilledCircle(color = red, diameter = size) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.58f)
                )
            }
            HabitStatus.PENDING -> Box(
                modifier = Modifier
                    .size(size)
                    .border(
                        width = size * 0.09f,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun FilledCircle(
    color: Color,
    diameter: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(diameter)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
