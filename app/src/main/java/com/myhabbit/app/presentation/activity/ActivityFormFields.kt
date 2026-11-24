package com.myhabbit.app.presentation.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myhabbit.app.domain.model.HabitCategory

/**
 * Field form activity (mockup UI.png #4) — dipakai bersama oleh dialog
 * "Tambah Activity" dan layar edit agar tampilannya identik.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormFields(
    state: ActivityFormUiState,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (HabitCategory) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormField(label = "Judul *", error = state.titleError) {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChange,
                placeholder = { Text("Mis. Meditasi pagi") },
                isError = state.titleError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        FormField(label = "Kategori", error = state.categoryError) {
            val selectedCategory = state.category
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory?.let { categoryLabel(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Pilih kategori") },
                    isError = state.categoryError != null,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    HabitCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(categoryLabel(category)) },
                            onClick = {
                                onCategoryChange(category)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
        }

        FormField(label = "Deskripsi (opsional)", error = state.descriptionError) {
            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                placeholder = { Text("Mis. Lakukan meditasi selama 10 menit") },
                isError = state.descriptionError != null,
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }

        state.error?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/** Tombol Batal + Simpan (mockup UI.png #4 — Simpan lebih lebar). */
@Composable
fun ActivityFormButtons(
    isSaving: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            enabled = !isSaving,
            modifier = Modifier.weight(1f)
        ) {
            Text("Batal")
        }
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier.weight(1.6f)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 2.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Simpan")
            }
        }
    }
}

/** Label di atas field + pesan error (mockup UI.png #4). */
@Composable
private fun FormField(
    label: String,
    error: String?,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
        error?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

fun categoryLabel(category: HabitCategory): String = when (category) {
    HabitCategory.DAILY -> "Harian"
    HabitCategory.WEEKLY -> "Mingguan"
    HabitCategory.MONTHLY -> "Bulanan"
}
