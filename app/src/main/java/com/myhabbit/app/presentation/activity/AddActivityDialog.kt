package com.myhabbit.app.presentation.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Popup "Tambah Kegiatan" (mockup UI.png #4) — form muncul sebagai kartu
 * di atas Beranda tanpa membuka halaman baru.
 */
@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetForm()
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) onSaved()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = !state.isSaving)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "Tambah Kegiatan",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ActivityFormFields(
                    state = state,
                    onTitleChange = viewModel::onTitleChange,
                    onCategoryChange = viewModel::onCategoryChange,
                    onDescriptionChange = viewModel::onDescriptionChange
                )

                Spacer(modifier = Modifier.height(20.dp))

                ActivityFormButtons(
                    isSaving = state.isSaving,
                    onCancel = onDismiss,
                    onSave = { viewModel.saveHabit(habitId = null) }
                )
            }
        }
    }
}
