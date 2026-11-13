package com.myhabbit.app.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onOpenActivityList: () -> Unit,
    onCreateActivity: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "MyHabbit Dashboard")
        Button(
            onClick = onCreateActivity,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Tambah Activity")
        }
        Button(
            onClick = onOpenActivityList,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Lihat Activity")
        }
    }
}
