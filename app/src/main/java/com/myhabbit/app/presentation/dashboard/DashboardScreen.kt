package com.myhabbit.app.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.myhabbit.app.ui.common.AppTopBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.usecase.record.HabitWithStatus
import com.myhabbit.app.presentation.activity.AddActivityDialog
import com.myhabbit.app.presentation.components.ProgressCard
import com.myhabbit.app.presentation.components.StatusIcon
import com.myhabbit.app.presentation.components.StatusPickerSheet
import com.myhabbit.app.ui.common.AppLogo
import com.myhabbit.app.ui.common.SwipeToDeleteContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onOpenActivityList: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProfile: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Konfirmasi hapus: "Restoring data" tidak berlaku di sini —
    // dialog delete activity (PRD.md §16)
    var habitToDelete by remember { mutableStateOf<HabitWithStatus?>(null) }

    // Popup "Pilih Status" (mockup UI.png #5) — kartu cukup 1 klik
    var statusTarget by remember { mutableStateOf<HabitWithStatus?>(null) }

    // Popup "Tambah Kegiatan" (mockup UI.png #4) — tanpa pindah halaman
    var showAddDialog by remember { mutableStateOf(false) }

    // Tab filter aktif (mockup UI.png #3) — default Harian
    var selectedTab by remember { mutableStateOf(HabitCategory.DAILY) }

    if (showAddDialog) {
        AddActivityDialog(
            onDismiss = { showAddDialog = false },
            onSaved = {
                showAddDialog = false
                viewModel.onActivityAdded()
            }
        )
    }

    statusTarget?.let { item ->
        StatusPickerSheet(
            onStatusSelected = { status ->
                viewModel.onStatusChange(item, status)
                statusTarget = null
            },
            onDismiss = { statusTarget = null }
        )
    }

    habitToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text("Delete Activity?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteHabit(item.habit.id)
                    habitToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { habitToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppLogo()
                        Text(
                            text = "MyHabbit",
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onOpenProfile,
                        modifier = Modifier.semantics { contentDescription = "Buka profil" }
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
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
                Button(onClick = { showAddDialog = true }) {
                    Text("Tambah Activity")
                }
                TextButton(onClick = onOpenActivityList) {
                    Text("Lihat semua activity")
                }
                TextButton(onClick = onOpenHistory) {
                    Text("Lihat history")
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

            // Tab filter (mockup UI.png #3): Harian / Mingguan / Bulanan
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    DashboardTab(
                        label = "Harian",
                        selected = selectedTab == HabitCategory.DAILY,
                        onClick = { selectedTab = HabitCategory.DAILY }
                    )
                    DashboardTab(
                        label = "Mingguan",
                        selected = selectedTab == HabitCategory.WEEKLY,
                        onClick = { selectedTab = HabitCategory.WEEKLY }
                    )
                    DashboardTab(
                        label = "Bulanan",
                        selected = selectedTab == HabitCategory.MONTHLY,
                        onClick = { selectedTab = HabitCategory.MONTHLY }
                    )
                }
            }

            val sectionItems = when (selectedTab) {
                HabitCategory.DAILY -> state.daily
                HabitCategory.WEEKLY -> state.weekly
                HabitCategory.MONTHLY -> state.monthly
            }

            habitSection(
                label = when (selectedTab) {
                    HabitCategory.DAILY -> "HARIAN"
                    HabitCategory.WEEKLY -> "MINGGUAN"
                    HabitCategory.MONTHLY -> "BULANAN"
                },
                items = sectionItems,
                onStatusChange = viewModel::onStatusChange,
                onDelete = { habitToDelete = it },
                onStatusClick = { statusTarget = it },
                emptyMessage = "Belum ada activity di kategori ini"
            )

            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    TextButton(onClick = onOpenActivityList) {
                        Text("Lihat semua activity")
                    }
                    TextButton(onClick = onOpenHistory) {
                        Text("Lihat history")
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

/** Tab filter (mockup UI.png #3): pill terisi ungu saat dipilih, outline saat tidak. */
@Composable
private fun DashboardTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(50.dp)
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .semantics { contentDescription = "Tab $label" }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

private fun LazyListScope.habitSection(
    label: String,
    items: List<HabitWithStatus>,
    onStatusChange: (HabitWithStatus, HabitStatus) -> Unit,
    onDelete: (HabitWithStatus) -> Unit,
    onStatusClick: (HabitWithStatus) -> Unit,
    emptyMessage: String? = null
) {
    item {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
    if (items.isEmpty()) {
        item {
            if (emptyMessage != null) {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }
        }
        return
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
        SwipeToDeleteContainer(
            deleteLabel = "Hapus",
            contentDescription = "Delete ${item.habit.title}",
            onDelete = { onDelete(item) }
        ) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    // Ikon status saat ini — ketuk untuk membuka popup
                    // "Pilih Status" (mockup UI.png #5)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onStatusClick(item) }
                            .semantics { contentDescription = "Pilih status ${item.habit.title}" },
                        contentAlignment = Alignment.Center
                    ) {
                        StatusIcon(status = item.status)
                    }
                }
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
