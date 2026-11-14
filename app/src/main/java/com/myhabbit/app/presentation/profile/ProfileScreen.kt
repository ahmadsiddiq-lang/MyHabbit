package com.myhabbit.app.presentation.profile

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.domain.model.User

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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

        DriveBackupCard(
            lastBackupAt = state.lastBackupAt,
            message = state.backupMessage,
            onBackupToDrive = { viewModel.backupToDrive(context) },
            onRestoreFromDrive = { viewModel.restoreFromDrive(context) },
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
