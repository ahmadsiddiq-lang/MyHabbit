package com.myhabbit.app.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.usecase.record.HabitWithStatus
import com.myhabbit.app.presentation.components.ProgressCard
import com.myhabbit.app.presentation.components.StatusSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onOpenActivityList: () -> Unit,
    onCreateActivity: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MyHabbit") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateActivity,
                modifier = Modifier.semantics { contentDescription = "Tambah activity" }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
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

        if (state.daily.isEmpty() && state.weekly.isEmpty() && state.monthly.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Belum ada activity")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onCreateActivity) {
                    Text("Tambah Activity")
                }
                TextButton(onClick = onOpenActivityList) {
                    Text("Lihat semua activity")
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = state.todayLabel,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (state.daily.isNotEmpty()) {
                habitSection(
                    label = "DAILY",
                    items = state.daily,
                    onStatusChange = viewModel::onStatusChange
                )
            }
            if (state.weekly.isNotEmpty()) {
                habitSection(
                    label = "WEEKLY",
                    items = state.weekly,
                    onStatusChange = viewModel::onStatusChange
                )
            }
            if (state.monthly.isNotEmpty()) {
                habitSection(
                    label = "MONTHLY",
                    items = state.monthly,
                    onStatusChange = viewModel::onStatusChange
                )
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

private fun LazyListScope.habitSection(
    label: String,
    items: List<HabitWithStatus>,
    onStatusChange: (HabitWithStatus, HabitStatus) -> Unit
) {
    item {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
    item {
        ProgressCard(
            label = "Progress $label",
            completed = items.count { it.status == HabitStatus.COMPLETED },
            evaluated = items.count { it.status != HabitStatus.PENDING },
            total = items.size,
            completionRate = completionRateOf(items)
        )
    }
    items(items.size) { index ->
        val item = items[index]
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.habit.title)
                }
                Spacer(modifier = Modifier.width(4.dp))
                StatusSelector(
                    currentStatus = item.status,
                    onStatusSelected = { onStatusChange(item, it) }
                )
            }
        }
    }
}

private fun completionRateOf(items: List<HabitWithStatus>): Double {
    val completed = items.count { it.status == HabitStatus.COMPLETED }
    val failed = items.count { it.status == HabitStatus.FAILED }
    val evaluated = completed + failed
    return if (evaluated == 0) 0.0 else completed.toDouble() / evaluated * 100
}
