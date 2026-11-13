package com.myhabbit.app.data.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Inject

/**
 * Bungkus Credential Manager (mekanisme resmi Google, PRD.md §31).
 *
 * Semua logic auth ada di sini — TIDAK di Compose screen
 * (ARCHITECTURE.md §14). Hanya menyimpan profil user, tanpa
 * password dan tanpa token.
 */
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(appContext)
    }

    suspend fun signIn(activityContext: Context): AuthResult {
        return try {
            val nonce = generateNonce()
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(AuthConfig.WEB_CLIENT_ID)
                .setNonce(nonce)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response: GetCredentialResponse =
                credentialManager.getCredential(activityContext, request)

            handleCredential(response)
        } catch (e: GetCredentialException) {
            AuthResult.Error("Login dibatalkan atau gagal: ${e.message ?: "unknown error"}")
        } catch (e: Exception) {
            AuthResult.Error("Terjadi kesalahan saat login: ${e.message ?: "unknown error"}")
        }
    }

    suspend fun signOut() {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) {
            // Clear state gagal tetap diabaikan — user lokal sudah dihapus di repository
        }
    }

    private fun handleCredential(response: GetCredentialResponse): AuthResult {
        val credential = response.credential
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return AuthResult.Error("Tipe credential tidak didukung")
        }

        return try {
            val token = GoogleIdTokenCredential.createFrom(credential.data)
            AuthResult.Success(
                User(
                    id = token.id,
                    googleId = token.id,
                    name = token.displayName ?: token.id,
                    email = extractEmail(token.idToken) ?: token.id,
                    photoUrl = token.profilePictureUri?.toString()
                )
            )
        } catch (e: Exception) {
            AuthResult.Error("Gagal membaca profil Google: ${e.message ?: "unknown error"}")
        }
    }

    /**
     * Email berada di payload ID token (claim "email"),
     * bukan properti langsung GoogleIdTokenCredential.
     */
    private fun extractEmail(idToken: String): String? {
        val parts = idToken.split(".")
        if (parts.size < 2) return null
        return try {
            val payload = String(Base64.getUrlDecoder().decode(parts[1]), Charsets.UTF_8)
            Regex("\"email\"\\s*:\\s*\"([^\"]+)\"").find(payload)?.groupValues?.get(1)
        } catch (_: Exception) {
            null
        }
    }

    private fun generateNonce(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
