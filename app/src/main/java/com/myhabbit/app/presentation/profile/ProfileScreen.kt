package com.myhabbit.app.presentation.profile

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.User

/** Sumber restore — untuk konfirmasi dialog (PRD.md §34). */
private enum class RestoreSource { FILE, DRIVE }

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Konfirmasi restore: "Restoring data will replace current data"
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Column
        }

        AccountCard(
            user = state.user,
            isSigningIn = state.isSigningIn,
            onSignIn = { viewModel.signIn(context) },
            onSignOut = { viewModel.signOut() }
        )

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = { viewModel.clearError() }) {
                Text("Tutup")
            }
        }

        AboutCard()

        ThemeCard(
            selected = state.theme,
            onSelect = { viewModel.onThemeChange(it) }
        )

        NotificationCard(
            enabled = state.notificationsEnabled,
            onToggle = { viewModel.onNotificationsChange(it) }
        )

        BackupCard(
            message = state.backupMessage,
            onExport = { viewModel.exportBackup() },
            onImport = { restoreSource = RestoreSource.FILE },
            onDismissMessage = { viewModel.clearBackupMessage() }
        )

        DriveBackupCard(
            lastBackupAt = state.lastBackupAt,
            message = state.backupMessage,
            onBackupToDrive = { withDriveConsent { viewModel.backupToDrive(context) } },
            onRestoreFromDrive = { restoreSource = RestoreSource.DRIVE },
            onDismissMessage = { viewModel.clearBackupMessage() }
        )

        Text(
            text = "MyHabbit v1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun AccountCard(
    user: User?,
    isSigningIn: Boolean,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.labelLarge
            )

            if (user == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Belum login",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Login untuk menghubungkan data dengan akun Google dan backup ke Drive",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = onSignIn,
                        enabled = !isSigningIn
                    ) {
                        if (isSigningIn) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text(
                            text = "Sign in with Google",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LetterAvatar(name = user.name)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Sign out")
                }
            }
        }
    }
}

@Composable
private fun BackupCard(
    message: String?,
    onExport: () -> Unit,
    onImport: () -> Unit,
    onDismissMessage: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Backup",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Simpan seluruh data ke file JSON, atau restore dari file backup. Restore akan mengganti data saat ini.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onExport) {
                    Text("Export backup")
                }
                OutlinedButton(onClick = onImport) {
                    Text("Import backup")
                }
            }
            message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = onDismissMessage) {
                    Text("Tutup")
                }
            }
        }
    }
}

@Composable
private fun DriveBackupCard(
    lastBackupAt: Long?,
    message: String?,
    onBackupToDrive: () -> Unit,
    onRestoreFromDrive: () -> Unit,
    onDismissMessage: () -> Unit
) {
    val lastBackupText = lastBackupAt?.let { time ->
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")
            .withZone(java.time.ZoneId.systemDefault())
        formatter.format(java.time.Instant.ofEpochMilli(time))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Backup Google Drive",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Simpan backup ke folder MyHabbit/ di Google Drive. Restore akan mengganti data saat ini.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (lastBackupText != null) "Last backup: $lastBackupText"
                else "Belum pernah backup ke Drive",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onBackupToDrive) {
                    Text("Backup ke Drive")
                }
                OutlinedButton(onClick = onRestoreFromDrive) {
                    Text("Restore dari Drive")
                }
            }
            message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = onDismissMessage) {
                    Text("Tutup")
                }
            }
        }
    }
}

@Composable
private fun ThemeCard(
    selected: String,
    onSelect: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.labelLarge
            )
            listOf(
                "system" to "System Default",
                "light" to "Light",
                "dark" to "Dark"
            ).forEach { (value, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selected == value,
                        onClick = { onSelect(value) }
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) onToggle(true)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Pengingat harian pukul 20:00 untuk menandai aktivitas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = { checked ->
                    if (checked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onToggle(checked)
                    }
                }
            )
        }
    }
}

@Composable
private fun LetterAvatar(name: String) {
    val initial = name.trim().take(1).uppercase().ifEmpty { "?" }
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun AboutCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "About",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "MyHabbit adalah habit tracker untuk membangun kebiasaan baik: buat activity, atur periode, tandai selesai, dan pantau progres lewat ringkasan.",
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalDivider()
            Text(
                text = "Data tersimpan lokal di perangkat. Login Google diperlukan hanya untuk backup ke Drive.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
