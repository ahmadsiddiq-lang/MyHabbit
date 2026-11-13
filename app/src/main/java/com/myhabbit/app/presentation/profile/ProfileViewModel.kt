package com.myhabbit.app.presentation.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import com.myhabbit.app.domain.repository.BackupRepository
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
    val backupMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val isSigningIn = MutableStateFlow(false)
    private val signInError = MutableStateFlow<String?>(null)
    private val backupMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.observeCurrentUser(),
        isSigningIn,
        signInError,
        backupMessage
    ) { user, signing, error, backup ->
        ProfileUiState(
            isLoading = false,
            user = user,
            isSigningIn = signing,
            error = error,
            backupMessage = backup
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
     *  import menimpa semua data, jadi konfirmasi dilakukan di UI. */
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

    fun importBackup() {
        viewModelScope.launch {
            val local = backupRepository.readBackupFile()
                ?: run {
                    backupMessage.value = "Belum ada file backup. Export dulu atau taruh file di storage aplikasi."
                    return@launch
                }
            backupMessage.value = when (val error = backupRepository.importFromJson(local)) {
                null -> "Restore berhasil — data berhasil diganti dari backup"
                else -> "Import gagal: $error"
            }
        }
    }

    fun clearBackupMessage() {
        backupMessage.value = null
    }
}
