package com.myhabbit.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Palet brand MyHabbit — sesuai mockup UI.png (ungu + aksen hijau/merah).
 * dynamicColor dimatikan agar tampilan konsisten dengan referensi UI.
 */

// Brand utama
val BrandPrimary = Color(0xFF5246C8)      // ungu tua — top bar, tombol utama
val BrandPrimaryDark = Color(0xFFC3BBFF)  // ungu muda — tema gelap
val BrandContainer = Color(0xFFE4DFFF)    // lavender terang
val BrandOnContainer = Color(0xFF1A1060)

// Aksen status
val SuccessGreen = Color(0xFF2E7D32)      // ✓ Selesai (terang)
val SuccessGreenDark = Color(0xFF8BD98F)  // ✓ Selesai (gelap)
val FailureRed = Color(0xFFB3261E)        // ✕ Tidak dilakukan (terang)
val FailureRedDark = Color(0xFFFFB4AB)    // ✕ Tidak dilakukan (gelap)

// Netral lama (dipertahankan untuk kompatibilitas)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
