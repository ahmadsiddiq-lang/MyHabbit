package com.myhabbit.app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.R
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.MenuRow

/**
 * Profil (mockup UI.png #8): avatar, nama, email, lalu daftar menu
 * Pengaturan / Backup ke Drive / Pulihkan dari Drive / Tentang Aplikasi / Keluar.
 */
@Composable
fun ProfileScreen(
    onOpenSettings: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenRestore: () -> Unit,
    onOpenAbout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppTopBar(title = { Text("Profil") })
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(user = state.user)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    MenuRow(
                        icon = painterResource(R.drawable.ic_settings),
                        label = "Pengaturan",
                        showChevron = true,
                        onClick = onOpenSettings,
                        divider = true
                    )
                    MenuRow(
                        icon = painterResource(R.drawable.ic_cloud_upload),
                        label = "Backup ke Google Drive",
                        showChevron = true,
                        onClick = onOpenBackup,
                        divider = true
                    )
                    MenuRow(
                        icon = painterResource(R.drawable.ic_cloud_download),
                        label = "Pulihkan dari Google Drive",
                        showChevron = true,
                        onClick = onOpenRestore,
                        divider = true
                    )
                    MenuRow(
                        icon = painterResource(R.drawable.ic_info),
                        label = "Tentang Aplikasi",
                        showChevron = true,
                        onClick = onOpenAbout
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                MenuRow(
                    icon = painterResource(R.drawable.ic_logout),
                    label = "Keluar",
                    labelColor = MaterialTheme.colorScheme.error,
                    onClick = { viewModel.signOut() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MyHabbit v1.0.0",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

/** Avatar bulat besar + nama + email (mockup #8). */
@Composable
private fun ProfileHeader(user: User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LetterAvatar(name = user?.name.orEmpty())
        Text(
            text = user?.name ?: "Belum login",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (user != null) {
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LetterAvatar(name: String) {
    val initial = name.trim().take(1).uppercase().ifEmpty { "?" }
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
