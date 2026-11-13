package com.myhabbit.app.presentation.profile

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isSigningIn: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val isSigningIn = MutableStateFlow(false)
    private val signInError = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.observeCurrentUser(),
        isSigningIn,
        signInError
    ) { user, signing, error ->
        ProfileUiState(
            isLoading = false,
            user = user,
            isSigningIn = signing,
            error = error
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
}
