package com.myhabbit.app.data.repository

import android.content.Context
import com.myhabbit.app.data.auth.GoogleAuthManager
import com.myhabbit.app.data.local.dao.UserDao
import com.myhabbit.app.data.local.entity.UserEntity
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Persist hanya info profil (ARCHITECTURE.md §14) —
 * tanpa password, tanpa token.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authManager: GoogleAuthManager,
    private val userDao: UserDao
) : AuthRepository {

    override fun observeCurrentUser(): Flow<User?> {
        return userDao.observeCurrentUser().map { entity -> entity?.toDomain() }
    }

    override suspend fun signIn(activityContext: Context): AuthResult {
        return when (val result = authManager.signIn(activityContext)) {
            is AuthResult.Success -> {
                userDao.insertUser(
                    UserEntity(
                        id = result.user.id,
                        googleId = result.user.googleId,
                        name = result.user.name,
                        email = result.user.email,
                        photoUrl = result.user.photoUrl,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                result
            }
            is AuthResult.Error -> result
        }
    }

    override suspend fun signOut() {
        authManager.signOut()
        userDao.deleteAllUsers()
    }
}
