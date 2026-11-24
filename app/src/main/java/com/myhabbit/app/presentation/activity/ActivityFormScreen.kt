package com.myhabbit.app.presentation.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.BackButton

/**
 * Layar Edit Activity. Penambahan activity memakai popup
 * ([AddActivityDialog]) agar tidak berpindah halaman.
 */
@Composable
fun ActivityFormScreen(
    habitId: String?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ActivityFormFields(
                        state = state,
                        onTitleChange = viewModel::onTitleChange,
                        onCategoryChange = viewModel::onCategoryChange,
                        onDescriptionChange = viewModel::onDescriptionChange
                    )

                    ActivityFormButtons(
                        isSaving = state.isSaving,
                        onCancel = onCancel,
                        onSave = { viewModel.saveHabit(habitId) }
                    )
                }
            }
        }
    }
}
