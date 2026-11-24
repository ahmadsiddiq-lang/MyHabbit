package com.myhabbit.app.presentation.backup

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.R
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.BackButton

/** Sumber restore — untuk konfirmasi dialog (PRD.md §34). */
private enum class RestoreSource { FILE, DRIVE }

/**
 * Backup & Restore (mockup UI.png #10): backup ke Google Drive,
 * pulihkan dari Google Drive, plus export/import file JSON lokal.
 */
@Composable
fun BackupScreen(
    onBack: () -> Unit,
    viewModel: BackupViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var restoreSource by remember { mutableStateOf<RestoreSource?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.importFromUri(context, it) }
    }

    // Konsen Drive via ActivityResult (polling authorize membatalkan sesi konsen)
    var pendingDriveAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    val driveConsentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val action = pendingDriveAction
        pendingDriveAction = null
        if (result.resultCode == Activity.RESULT_OK) {
            action?.invoke()
        } else {
            viewModel.onDriveConsentCanceled()
        }
    }

    /** Jalankan aksi Drive setelah memastikan konsen diberikan. */
    fun withDriveConsent(action: () -> Unit) {
        viewModel.requestDriveConsent(context) { sender ->
            if (sender == null) {
                action()
            } else {
                pendingDriveAction = action
                driveConsentLauncher.launch(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }

    restoreSource?.let { source ->
        AlertDialog(
            onDismissRequest = { restoreSource = null },
            title = { Text("Restore data") },
            text = {
                Text("Restoring data will replace current data.\nSemua data saat ini akan diganti dengan isi backup.")
            },
            confirmButton = {
                TextButton(onClick = {
                    restoreSource = null
                    when (source) {
                        RestoreSource.FILE -> filePicker.launch(
                            arrayOf("application/json", "text/plain")
                        )
                        RestoreSource.DRIVE -> withDriveConsent {
                            viewModel.restoreFromDrive(context)
                        }
                    }
                }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { restoreSource = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = { Text("Backup & Restore") },
                navigationIcon = { BackButton(onBack = onBack) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.message?.let { message ->
                Column {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    TextButton(onClick = { viewModel.clearMessage() }) {
                        Text("Tutup")
                    }
                }
            }

            DriveActionCard(
                iconRes = R.drawable.ic_cloud_upload,
                title = "Backup ke Google Drive",
                description = "Simpan data aplikasi Anda ke Google Drive.",
                buttonLabel = "Backup Sekarang",
                onClick = { withDriveConsent { viewModel.backupToDrive(context) } }
            )

            DriveActionCard(
                iconRes = R.drawable.ic_cloud_download,
                title = "Pulihkan dari Google Drive",
                description = "Ambil data dari Google Drive.",
                buttonLabel = "Restore Sekarang",
                onClick = { restoreSource = RestoreSource.DRIVE }
            )

            Text(
                text = if (state.lastBackupAt != null) "Last backup: ${formatTimestamp(state.lastBackupAt!!)}"
                else "Belum pernah backup ke Drive",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "File JSON lokal",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Export ke file JSON di perangkat, atau restore dari file backup.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.exportBackup() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Export backup")
                        }
                        OutlinedButton(
                            onClick = { restoreSource = RestoreSource.FILE },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Import backup")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DriveActionCard(
    iconRes: Int,
    title: String,
    description: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(buttonLabel)
            }
        }
    }
}

private fun formatTimestamp(time: Long): String {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")
        .withZone(java.time.ZoneId.systemDefault())
    return formatter.format(java.time.Instant.ofEpochMilli(time))
}
