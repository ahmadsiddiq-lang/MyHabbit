package com.myhabbit.app.presentation.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.R
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.BackButton
import com.myhabbit.app.ui.common.MenuRow

/** Reset mana yang menunggu konfirmasi user (PRD.md §34a — confirm dulu). */
private enum class PendingReset { RECORDS, ALL }

private val themeLabels = listOf(
    "system" to "System Default",
    "light" to "Terang",
    "dark" to "Gelap"
)

/**
 * Pengaturan (mockup UI.png #9): notifikasi, tema aplikasi, reset data.
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showThemeDialog by remember { mutableStateOf(false) }
    var pending by remember { mutableStateOf<PendingReset?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.onNotificationsChange(true)
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Tema Aplikasi") },
            text = {
                Column {
                    themeLabels.forEach { (value, label) ->
                        ThemeOptionRow(
                            label = label,
                            selected = state.theme == value,
                            onClick = {
                                viewModel.onThemeChange(value)
                                showThemeDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    pending?.let { reset ->
        AlertDialog(
            onDismissRequest = { pending = null },
            title = {
                Text(
                    if (reset == PendingReset.RECORDS) "Reset summary data?"
                    else "Reset ALL data?"
                )
            },
            text = {
                Text(
                    if (reset == PendingReset.RECORDS)
                        "All status history will be deleted. Activities are kept. This action cannot be undone."
                    else
                        "All activities and history will be deleted. This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (reset == PendingReset.RECORDS) viewModel.resetRecords()
                    else viewModel.resetAllData()
                    pending = null
                }) {
                    Text(
                        text = if (reset == PendingReset.RECORDS) "Reset" else "Delete everything",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pending = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = { Text("Pengaturan") },
                navigationIcon = { BackButton(onBack = onBack) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            state.message?.let { message ->
                Column {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    TextButton(
                        onClick = { viewModel.clearMessage() },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text("Tutup")
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    MenuRow(
                        icon = painterResource(R.drawable.ic_notifications),
                        label = "Notifikasi",
                        subtitle = "Pengingat harian pukul 20:00",
                        trailing = {
                            Switch(
                                checked = state.notificationsEnabled,
                                onCheckedChange = { checked ->
                                    if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        viewModel.onNotificationsChange(checked)
                                    }
                                }
                            )
                        },
                        divider = true
                    )
                    MenuRow(
                        icon = painterResource(R.drawable.ic_palette),
                        label = "Tema Aplikasi",
                        subtitle = themeLabels.firstOrNull { it.first == state.theme }?.second,
                        showChevron = true,
                        onClick = { showThemeDialog = true }
                    )
                }
            }

            Text(
                text = "Reset Data",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
            )
            Text(
                text = "Reset Ringkasan menghapus semua history/status — activity tetap ada. " +
                    "Hard reset menghapus SEMUA activity dan history. Backup dulu disarankan.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    MenuRow(
                        icon = painterResource(R.drawable.ic_reset),
                        label = "Reset Ringkasan",
                        labelColor = MaterialTheme.colorScheme.error,
                        onClick = { pending = PendingReset.RECORDS },
                        divider = true
                    )
                    MenuRow(
                        icon = painterResource(R.drawable.ic_reset),
                        label = "Hard Reset Semua Data",
                        labelColor = MaterialTheme.colorScheme.error,
                        onClick = { pending = PendingReset.ALL }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
