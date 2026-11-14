package com.myhabbit.app.domain.model

/**
 * Hasil operasi autentikasi (ARCHITECTURE.md §14).
 */
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
