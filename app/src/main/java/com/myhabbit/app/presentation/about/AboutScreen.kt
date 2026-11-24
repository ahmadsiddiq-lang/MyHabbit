package com.myhabbit.app.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myhabbit.app.ui.common.AppTopBar
import com.myhabbit.app.ui.common.BackButton

/** Tentang Aplikasi (mockup UI.png #8 — baris "Tentang Aplikasi"). */
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = { Text("Tentang Aplikasi") },
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
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "MyHabbit",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Versi 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "MyHabbit adalah habit tracker untuk membangun kebiasaan baik: " +
                            "buat activity, atur periode, tandai selesai, dan pantau progres " +
                            "lewat ringkasan.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Data & Privasi",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Data tersimpan lokal di perangkat. Login Google diperlukan " +
                            "hanya untuk backup ke Drive.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
