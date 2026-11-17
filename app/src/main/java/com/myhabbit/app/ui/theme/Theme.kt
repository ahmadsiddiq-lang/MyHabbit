package com.myhabbit.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Palet brand tetap (mockup UI.png) — dynamicColor Material You TIDAK
 * dipakai agar warna aplikasi selalu konsisten dengan referensi UI.
 */
private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimaryDark,
    onPrimary = Color(0xFF241A6B),
    primaryContainer = Color(0xFF3B2F8F),
    onPrimaryContainer = Color(0xFFE4DFFF),
    secondary = Color(0xFFC6C4E0),
    onSecondary = Color(0xFF2E2B4D),
    secondaryContainer = Color(0xFF45426A),
    onSecondaryContainer = Color(0xFFE2E0F9),
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF121218),
    onBackground = Color(0xFFE4E1EC),
    surface = Color(0xFF121218),
    onSurface = Color(0xFFE4E1EC),
    surfaceVariant = Color(0xFF45425A),
    onSurfaceVariant = Color(0xFFC6C4D9),
    error = FailureRedDark,
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = BrandContainer,
    onPrimaryContainer = BrandOnContainer,
    secondary = Color(0xFF5C5A96),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2E0F9),
    onSecondaryContainer = Color(0xFF191A4C),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFCFBFF),
    onBackground = Color(0xFF1B1B21),
    surface = Color(0xFFFCFBFF),
    onSurface = Color(0xFF1B1B21),
    surfaceVariant = Color(0xFFE4E1F5),
    onSurfaceVariant = Color(0xFF46464F),
    error = FailureRed,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun MyHabbitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
