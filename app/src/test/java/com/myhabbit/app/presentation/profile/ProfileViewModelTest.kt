package com.myhabbit.app.presentation.profile

import android.content.Context
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import com.myhabbit.app.domain.repository.BackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var backupRepository: BackupRepository
    private val currentUserFlow = MutableStateFlow<User?>(null)
    private val activityContext: Context = Mockito.mock(Context::class.java)

    private val testUser = User(
        id = "gid",
        googleId = "gid",
        name = "Ahmad",
        email = "ahmad@example.com",
        photoUrl = null
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = Mockito.mock(AuthRepository::class.java)
        backupRepository = Mockito.mock(BackupRepository::class.java)
        Mockito.`when`(authRepository.observeCurrentUser()).thenReturn(currentUserFlow)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initiallySignedOut() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(authRepository, backupRepository)

        val state = viewModel.uiState.first { !it.isLoading }

        assertNull(state.user)
        assertFalse(state.isSigningIn)
        assertNull(state.error)
    }

    @Test
    fun signIn_success_updatesUser() = runTest(testDispatcher) {
        Mockito.`when`(authRepository.signIn(activityContext)).thenAnswer {
            currentUserFlow.value = testUser
            AuthResult.Success(testUser)
        }
        val viewModel = ProfileViewModel(authRepository, backupRepository)
        viewModel.uiState.first { !it.isLoading }

        viewModel.signIn(activityContext)

        val state = viewModel.uiState.first { it.user != null && !it.isSigningIn }
        assertEquals("Ahmad", state.user?.name)
        assertNull(state.error)
    }

    @Test
    fun signIn_error_setsErrorWithoutUser() = runTest(testDispatcher) {
        Mockito.`when`(authRepository.signIn(activityContext))
            .thenReturn(AuthResult.Error("Login dibatalkan"))
        val viewModel = ProfileViewModel(authRepository, backupRepository)
        viewModel.uiState.first { !it.isLoading }

        viewModel.signIn(activityContext)

        val state = viewModel.uiState.first { it.error != null }
        assertEquals("Login dibatalkan", state.error)
        assertNull(state.user)
    }

    @Test
    fun signOut_clearsUser() = runTest(testDispatcher) {
        currentUserFlow.value = testUser
        Mockito.`when`(authRepository.signOut()).thenAnswer {
            currentUserFlow.value = null
            Unit
        }
        val viewModel = ProfileViewModel(authRepository, backupRepository)
        viewModel.uiState.first { it.user != null }

        viewModel.signOut()

        viewModel.uiState.first { it.user == null && !it.isSigningIn }
        val state = viewModel.uiState.value
        assertNull(state.user)
    }

    @Test
    fun exportBackup_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson()).thenReturn("{\"schemaVersion\":1}")
        val viewModel = ProfileViewModel(authRepository, backupRepository)
        viewModel.uiState.first { !it.isLoading }

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("berhasil") == true)
    }

    @Test
    fun exportBackup_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson())
            .thenThrow(RuntimeException("disk full"))
        val viewModel = ProfileViewModel(authRepository, backupRepository)
        viewModel.uiState.first { !it.isLoading }

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("gagal") == true)
        assertTrue(state.backupMessage?.contains("disk full") == true)
    }
}
