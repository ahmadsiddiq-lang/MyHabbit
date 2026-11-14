package com.myhabbit.app.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.usecase.history.HistoryEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("History") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.filter == HistoryFilter.THIS_WEEK,
                    onClick = { viewModel.onFilterChange(HistoryFilter.THIS_WEEK) },
                    label = { Text("This Week") }
                )
                FilterChip(
                    selected = state.filter == HistoryFilter.THIS_MONTH,
                    onClick = { viewModel.onFilterChange(HistoryFilter.THIS_MONTH) },
                    label = { Text("This Month") }
                )
                FilterChip(
                    selected = state.filter == HistoryFilter.ALL,
                    onClick = { viewModel.onFilterChange(HistoryFilter.ALL) },
                    label = { Text("All") }
                )
            }

            if (state.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            if (state.entries.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tidak ada history untuk filter ini")
                }
                return@Column
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.entries, key = { "${it.habitId}-${it.periodKey}" }) { entry ->
                    HistoryItem(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(entry: HistoryEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.habitTitle,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = entry.periodKey,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            StatusIcon(status = entry.status)
        }
    }
}

@Composable
private fun StatusIcon(status: HabitStatus) {
    when (status) {
        HabitStatus.PENDING -> {
            Text(text = "○", style = MaterialTheme.typography.titleMedium)
        }
        HabitStatus.COMPLETED -> {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selesai",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(22.dp)
            )
        }
        HabitStatus.FAILED -> {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Tidak dilakukan",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
