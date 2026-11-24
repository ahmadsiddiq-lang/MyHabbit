package com.myhabbit.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.repository.SettingsRepository
import com.myhabbit.app.domain.usecase.habit.ResetAllDataUseCase
import com.myhabbit.app.domain.usecase.habit.ResetRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val theme: String = "system",
    val notificationsEnabled: Boolean = false,
    val message: String? = null
)

/**
 * Pengaturan aplikasi (mockup UI.png #9): notifikasi, tema, reset data.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val resetRecordsUseCase: ResetRecordsUseCase,
    private val resetAllDataUseCase: ResetAllDataUseCase
) : ViewModel() {

    private val message = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.observeTheme(),
        settingsRepository.observeNotificationsEnabled(),
        message
    ) { theme, notifications, msg ->
        SettingsUiState(
            theme = theme,
            notificationsEnabled = notifications,
            message = msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

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

    fun clearMessage() {
        message.value = null
    }

    /** Reset ringkasan: hapus semua records, activity tetap (PRD.md §34a.1).
     *  Dipanggil SETELAH konfirmasi user. */
    fun resetRecords() {
        viewModelScope.launch {
            message.value = when (val result = resetRecordsUseCase()) {
                is AppResult.Success -> "Ringkasan berhasil direset — history mulai dari nol"
                is AppResult.Error -> result.message
            }
        }
    }

    /** Hard reset: hapus SEMUA activity + history (PRD.md §34a.2).
     *  Dipanggil SETELAH konfirmasi user. */
    fun resetAllData() {
        viewModelScope.launch {
            message.value = when (val result = resetAllDataUseCase()) {
                is AppResult.Success -> "Semua data berhasil direset — aplikasi kembali seperti baru"
                is AppResult.Error -> result.message
            }
        }
    }
}
