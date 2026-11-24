package com.myhabbit.app.presentation.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormScreen(
    habitId: String?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    var categoryExpanded by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }

    LaunchedEffect(habitId) {
        if (!loaded) {
            if (habitId != null) {
                viewModel.loadHabit(habitId)
            } else {
                viewModel.resetForm()
            }
            loaded = true
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSaved()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = { Text(if (habitId == null) "Tambah Activity" else "Edit Activity") },
                navigationIcon = { BackButton(onBack = onCancel) }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FormField(label = "Judul *", error = state.titleError) {
                        OutlinedTextField(
                            value = state.title,
                            onValueChange = viewModel::onTitleChange,
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
                                            viewModel.onCategoryChange(category)
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
                            onValueChange = viewModel::onDescriptionChange,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            enabled = !state.isSaving,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Batal")
                        }
                        Button(
                            onClick = { viewModel.saveHabit(habitId) },
                            enabled = !state.isSaving,
                            modifier = Modifier.weight(1.6f)
                        ) {
                            if (state.isSaving) {
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

private fun categoryLabel(category: HabitCategory): String = when (category) {
    HabitCategory.DAILY -> "Harian"
    HabitCategory.WEEKLY -> "Mingguan"
    HabitCategory.MONTHLY -> "Bulanan"
}
