package com.myhabbit.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null
)

/**
 * Profil (mockup UI.png #8): identitas akun Google + keluar.
 * Login dilakukan di layar Login; pengaturan & backup ada di layar sendiri.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = authRepository.observeCurrentUser()
        .map { user -> ProfileUiState(isLoading = false, user = user) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState()
        )

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
