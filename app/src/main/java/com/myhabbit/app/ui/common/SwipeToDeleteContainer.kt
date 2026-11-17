package com.myhabbit.app.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Container dengan interaksi "geser untuk hapus" (PRD.md §16):
 * konten [content] bisa digeser ke KIRI untuk menampilkan tombol
 * "Hapus" merah di sisi kanan. Ketik tombol → [onDelete] dipanggil
 * (UI pemanggil wajib menampilkan dialog konfirmasi sebelum benar-benar
 * menghapus). Geser kembali ke kanan menutup tombol.
 */
@Composable
fun SwipeToDeleteContainer(
    deleteLabel: String,
    contentDescription: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val revealWidthPx = with(LocalDensity.current) { 88.dp.toPx() }
    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
    ) {
        // Area merah "Hapus" — tersembunyi di belakang konten
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = {
                    scope.launch { offsetX.animateTo(0f) }
                    onDelete()
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = contentDescription,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(deleteLabel)
            }
        }

        // Konten utama — digeser dengan drag horizontal
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(revealWidthPx) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val target =
                                    if (offsetX.value < -revealWidthPx / 2) -revealWidthPx else 0f
                                offsetX.animateTo(target, tween(200))
                            }
                        },
                        onDragCancel = {
                            scope.launch {
                                val target =
                                    if (offsetX.value < -revealWidthPx / 2) -revealWidthPx else 0f
                                offsetX.animateTo(target, tween(200))
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount)
                                    .coerceIn(-revealWidthPx, 0f)
                            )
                        }
                    }
                }
        ) {
            content()
        }
    }
}
