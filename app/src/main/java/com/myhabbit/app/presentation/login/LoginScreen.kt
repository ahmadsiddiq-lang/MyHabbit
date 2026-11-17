package com.myhabbit.app.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myhabbit.app.R
import com.myhabbit.app.ui.common.AppLogo
import com.myhabbit.app.ui.theme.BrandPrimary

/**
 * Layar awal (mockup UI.png #2): latar gradient ungu, logo + nama + tagline,
 * kartu putih berisi tombol "Masuk dengan Google".
 * HANYA Google Sign-In — opsi email dari mockup tidak diimplementasi
 * (keputusan user, PRD.md §82.4).
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Bersihkan pesan error saat layar dibuka ulang
    LaunchedEffect(Unit) { viewModel.clearError() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Gradient diagonal mockup #2: terang di kiri atas → ungu brand
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8F83F5),
                        Color(0xFF6A5AE0),
                        Color(0xFF5246C8)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AppLogo(
                size = 96.dp,
                container = Color.White,
                iconColor = BrandPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MyHabbit",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track • Plan • Achieve",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )

            // Kartu putih (mockup #2) di TENGAH layar, bukan menempel bawah
            Spacer(modifier = Modifier.height(36.dp))

            // Kartu putih (mockup #2) — warna tetap agar konsisten di tema
            // terang/gelap (kartu selalu putih dengan teks gelap)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selamat datang",
                    color = Color(0xFF1B1B21),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Masuk untuk melacak kebiasaan baikmu",
                    color = Color(0xFF46464F),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                GoogleSignInButton(
                    enabled = !state.isSigningIn,
                    onClick = { viewModel.signIn(context) }
                )

                if (state.isSigningIn) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = BrandPrimary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }

                state.error?.let { error ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error,
                        color = Color(0xFFB3261E),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))
        }
    }
}

/** Tombol "Masuk dengan Google" (mockup #2): putih ber-border + logo G. */
@Composable
private fun GoogleSignInButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFDADCE0),
                shape = RoundedCornerShape(50.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .semantics { contentDescription = "Masuk dengan Google" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Masuk dengan Google",
            color = Color(0xFF1B1B21),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
