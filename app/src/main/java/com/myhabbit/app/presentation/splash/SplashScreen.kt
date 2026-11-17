package com.myhabbit.app.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myhabbit.app.ui.common.AppLogo
import com.myhabbit.app.ui.theme.BrandPrimary

/**
 * Splash screen (mockup UI.png layar #1): latar ungu, logo centang,
 * nama aplikasi + tagline "Track • Plan • Achieve", spinner di bawah.
 * Ditampilkan singkat sebelum konten utama (lihat AppNavigation).
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A5AE0),
                        BrandPrimary,
                        Color(0xFF3D34A0)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }

        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 3.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
                .size(32.dp)
        )
    }
}
