package com.myhabbit.app.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.myhabbit.app.R

/** Tombol panah kembali di top bar (mockup UI.png #8/#9/#10). */
@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Kembali"
        )
    }
}
