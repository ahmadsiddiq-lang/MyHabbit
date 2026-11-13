package com.myhabbit.app.data.repository

import android.content.Context
import com.myhabbit.app.data.auth.GoogleAuthManager
import com.myhabbit.app.data.local.dao.UserDao
import com.myhabbit.app.data.local.entity.UserEntity
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AuthRepositoryImplTest {

    private lateinit var authManager: GoogleAuthManager
    private lateinit var userDao: FakeUserDao
    private lateinit var repository: AuthRepositoryImpl

    private val activityContext: Context = Mockito.mock(Context::class.java)

    private val testUser = User(
        id = "google-id-123",
        googleId = "google-id-123",
        name = "Ahmad",
        email = "ahmad@example.com",
        photoUrl = "https://photo.url"
    )

    /** Fake sederhana: tidak butuh stubbing suspend di setup. */
    private class FakeUserDao : UserDao {
        val users = MutableStateFlow<UserEntity?>(null)
        var deleteAllCalled = false

        override suspend fun getUserById(id: String): UserEntity? = users.value

        override fun observeCurrentUser(): Flow<UserEntity?> = users

        override suspend fun insertUser(user: UserEntity) {
            users.value = user
        }

        override suspend fun deleteAllUsers() {
            deleteAllCalled = true
            users.value = null
        }
    }

    @Before
    fun setup() {
        authManager = Mockito.mock(GoogleAuthManager::class.java)
        userDao = FakeUserDao()
        repository = AuthRepositoryImpl(authManager, userDao)
    }

    @Test
    fun signIn_success_persistsProfile() = runTest {
        Mockito.`when`(authManager.signIn(activityContext))
            .thenReturn(AuthResult.Success(testUser))

        val result = repository.signIn(activityContext)

        assertTrue(result is AuthResult.Success)
        val saved = userDao.users.value
        assertNotNull(saved)
        assertEquals("google-id-123", saved?.id)
        assertEquals("google-id-123", saved?.googleId)
        assertEquals("Ahmad", saved?.name)
        assertEquals("ahmad@example.com", saved?.email)
        assertEquals("https://photo.url", saved?.photoUrl)
    }

    @Test
    fun signIn_error_doesNotPersist() = runTest {
        Mockito.`when`(authManager.signIn(activityContext))
            .thenReturn(AuthResult.Error("Login dibatalkan"))

        val result = repository.signIn(activityContext)

        assertEquals("Login dibatalkan", (result as AuthResult.Error).message)
        assertNull(userDao.users.value)
    }

    @Test
    fun signOut_clearsUserAndCallsManager() = runTest {
        userDao.users.value = UserEntity(
            id = "google-id-123",
            googleId = "google-id-123",
            name = "Ahmad",
            email = "ahmad@example.com",
            photoUrl = null,
            createdAt = 1L,
            updatedAt = 1L
        )

        repository.signOut()

        Mockito.verify(authManager).signOut()
        assertTrue(userDao.deleteAllCalled)
        assertNull(repository.observeCurrentUser().first())
    }

    @Test
    fun observeCurrentUser_mapsToDomain() = runTest {
        userDao.users.value = UserEntity(
            id = "gid",
            googleId = "gid",
            name = "Budi",
            email = "budi@example.com",
            photoUrl = null,
            createdAt = 1L,
            updatedAt = 1L
        )

        val user = repository.observeCurrentUser().first()

        assertEquals("Budi", user?.name)
        assertEquals("budi@example.com", user?.email)
        assertEquals("gid", user?.googleId)
    }
}
