package com.myhabbit.app.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import com.myhabbit.app.domain.repository.BackupRepository
import com.myhabbit.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isSigningIn: Boolean = false,
    val error: String? = null,
    val backupMessage: String? = null,
    val lastBackupAt: Long? = null,
    val theme: String = "system",
    val notificationsEnabled: Boolean = false
)

/** State inti — di-combine dulu agar tidak melebihi 5 flow per combine. */
private data class ProfileCoreState(
    val user: User? = null,
    val isSigningIn: Boolean = false,
    val error: String? = null,
    val backupMessage: String? = null,
    val lastBackupAt: Long? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val backupRepository: BackupRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val isSigningIn = MutableStateFlow(false)
    private val signInError = MutableStateFlow<String?>(null)
    private val backupMessage = MutableStateFlow<String?>(null)

    private val coreState = combine(
        authRepository.observeCurrentUser(),
        isSigningIn,
        signInError,
        backupMessage,
        backupRepository.observeLastBackupAt()
    ) { user, signing, error, backup, lastBackup ->
        ProfileCoreState(user, signing, error, backup, lastBackup)
    }

    val uiState: StateFlow<ProfileUiState> = combine(
        coreState,
        settingsRepository.observeTheme(),
        settingsRepository.observeNotificationsEnabled()
    ) { core, theme, notifications ->
        ProfileUiState(
            isLoading = false,
            user = core.user,
            isSigningIn = core.isSigningIn,
            error = core.error,
            backupMessage = core.backupMessage,
            lastBackupAt = core.lastBackupAt,
            theme = theme,
            notificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    /**
     * ActivityContext dibutuhkan Credential Manager untuk menampilkan
     * sheet login; logic tetap di repository (ARCHITECTURE.md §14).
     */
    fun signIn(activityContext: Context) {
        viewModelScope.launch {
            isSigningIn.value = true
            signInError.value = null
            when (val result = authRepository.signIn(activityContext)) {
                is AuthResult.Success -> Unit
                is AuthResult.Error -> signInError.value = result.message
            }
            isSigningIn.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun clearError() {
        signInError.value = null
    }

    /** Export: "Restoring data will replace current data" (PRD.md §34) —
     *  import menimpa semua data, konfirmasi dilakukan di UI. */
    fun exportBackup() {
        viewModelScope.launch {
            backupMessage.value = try {
                backupRepository.exportToJson()
                "Backup berhasil disimpan ke storage aplikasi"
            } catch (e: Exception) {
                "Backup gagal: ${e.message ?: "unknown error"}"
            }
        }
    }

    /** Import dari file pilihan user (SAF) — dipanggil SETELAH konfirmasi. */
    fun importFromUri(activityContext: Context, uri: Uri) {
        viewModelScope.launch {
            backupMessage.value = when (val error = backupRepository.importFromUri(activityContext, uri)) {
                null -> "Restore berhasil — data berhasil diganti dari file"
                else -> "Import gagal: $error"
            }
        }
    }

    fun clearBackupMessage() {
        backupMessage.value = null
    }

    fun backupToDrive(activityContext: Context) {
        viewModelScope.launch {
            backupMessage.value = when (val error = backupRepository.backupToDrive(activityContext)) {
                null -> "Backup berhasil disimpan ke Google Drive"
                else -> "Backup ke Drive gagal: $error"
            }
        }
    }

    fun restoreFromDrive(activityContext: Context) {
        viewModelScope.launch {
            backupMessage.value = when (val error = backupRepository.restoreFromDrive(activityContext)) {
                null -> "Restore berhasil — data berhasil diganti dari Drive"
                else -> "Restore dari Drive gagal: $error"
            }
        }
    }

    fun onThemeChange(theme: String) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun onNotificationsChange(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }
}
