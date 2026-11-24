package com.myhabbit.app.presentation.backup

import android.content.Context
import android.content.IntentSender
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.repository.BackupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BackupUiState(
    val message: String? = null,
    val lastBackupAt: Long? = null
)

/**
 * Backup & Restore (mockup UI.png #10): backup ke Drive, pulihkan dari Drive,
 * plus export/import file JSON lokal (PRD.md §33, §34).
 */
@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BackupUiState> = combine(
        message,
        backupRepository.observeLastBackupAt()
    ) { msg, lastBackup ->
        BackupUiState(message = msg, lastBackupAt = lastBackup)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BackupUiState()
    )

    fun exportBackup() {
        viewModelScope.launch {
            message.value = try {
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
            message.value = when (val error = backupRepository.importFromUri(activityContext, uri)) {
                null -> "Restore berhasil — data berhasil diganti dari file"
                else -> "Import gagal: $error"
            }
        }
    }

    fun clearMessage() {
        message.value = null
    }

    /**
     * Cek otorisasi Drive dulu. Bila user belum pernah konsen, kirim
     * IntentSender ke [onResult] — UI meluncurkan dialog konsen via
     * ActivityResult; bila null, aksi bisa langsung dijalankan.
     */
    fun requestDriveConsent(activityContext: Context, onResult: (IntentSender?) -> Unit) {
        viewModelScope.launch {
            onResult(backupRepository.prepareDriveConsent(activityContext))
        }
    }

    fun onDriveConsentCanceled() {
        message.value = "Otorisasi Drive dibatalkan"
    }

    fun backupToDrive(activityContext: Context) {
        viewModelScope.launch {
            message.value = when (val error = backupRepository.backupToDrive(activityContext)) {
                null -> "Backup berhasil disimpan ke Google Drive"
                else -> "Backup ke Drive gagal: $error"
            }
        }
    }

    fun restoreFromDrive(activityContext: Context) {
        viewModelScope.launch {
            message.value = when (val error = backupRepository.restoreFromDrive(activityContext)) {
                null -> "Restore berhasil — data berhasil diganti dari Drive"
                else -> "Restore dari Drive gagal: $error"
            }
        }
    }
}
