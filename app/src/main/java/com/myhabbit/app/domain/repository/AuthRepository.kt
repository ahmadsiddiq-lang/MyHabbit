package com.myhabbit.app.domain.repository

import android.content.Context
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository autentikasi (ARCHITECTURE.md §14).
 *
 * Aturan: tanpa password, tanpa token di storage,
 * profil user saja yang dipersist.
 */
interface AuthRepository {

    /** User yang sedang login (null = belum login). */
    fun observeCurrentUser(): Flow<User?>

    suspend fun signIn(activityContext: Context): AuthResult

    suspend fun signOut()
}
