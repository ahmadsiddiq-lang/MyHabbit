package com.myhabbit.app.data.auth

/**
 * Konfigurasi autentikasi Google.
 *
 * GANTI nilai di bawah dengan **Web Client ID** dari
 * Google Cloud Console → APIs & Services → Credentials → OAuth 2.0 Client ID
 * (Application type: Web application).
 *
 * SHA-1 debug keystore juga harus didaftarkan di OAuth client tersebut.
 * Lihat catatan di PROMPT.md (Phase 10).
 */
object AuthConfig {
    const val WEB_CLIENT_ID = "569827960080-blim8ls8a85v16980elc5ej1prsk5biv.apps.googleusercontent.com"
}
