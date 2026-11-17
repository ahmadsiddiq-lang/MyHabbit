package com.myhabbit.app.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isSigningIn: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    /** true setelah status user dibaca pertama kali (anti-flash saat sudah login). */
    val authChecked: Boolean = false
)

/**
 * ViewModel layar awal Login Google (mockup UI.png #2).
 * HANYA Google Sign-In — tanpa email/password (PRD.md §82.4).
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val isSigningIn = MutableStateFlow(false)
    private val signInError = MutableStateFlow<String?>(null)
    private val authChecked = MutableStateFlow(false)

    val uiState: StateFlow<LoginUiState> = combine(
        authRepository.observeCurrentUser().onEach { authChecked.value = true },
        isSigningIn,
        signInError,
        authChecked
    ) { user, signing, error, checked ->
        LoginUiState(
            isSigningIn = signing,
            error = error,
            user = user,
            authChecked = checked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginUiState()
    )

    /**
     * ActivityContext dibutuhkan Credential Manager untuk menampilkan
     * sheet akun Google; logic tetap di repository (ARCHITECTURE.md §14).
     */
    fun signIn(activityContext: Context) {
        viewModelScope.launch {
            isSigningIn.value = true
            signInError.value = null
            when (val result = authRepository.signIn(activityContext)) {
                is AuthResult.Success -> Unit // observeCurrentUser akan memancarkan user
                is AuthResult.Error -> signInError.value = result.message
            }
            isSigningIn.value = false
        }
    }

    fun clearError() {
        signInError.value = null
    }
}
