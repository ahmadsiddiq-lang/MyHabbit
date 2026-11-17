package com.myhabbit.app.presentation.profile

import android.content.Context
import android.content.IntentSender
import android.net.Uri
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import com.myhabbit.app.domain.repository.BackupRepository
import com.myhabbit.app.domain.repository.SettingsRepository
import com.myhabbit.app.domain.usecase.habit.ResetAllDataUseCase
import com.myhabbit.app.domain.usecase.habit.ResetRecordsUseCase
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
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var resetRecordsUseCase: ResetRecordsUseCase
    private lateinit var resetAllDataUseCase: ResetAllDataUseCase
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
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        resetRecordsUseCase = Mockito.mock(ResetRecordsUseCase::class.java)
        resetAllDataUseCase = Mockito.mock(ResetAllDataUseCase::class.java)
        Mockito.`when`(authRepository.observeCurrentUser()).thenReturn(currentUserFlow)
        Mockito.`when`(backupRepository.observeLastBackupAt())
            .thenReturn(MutableStateFlow<Long?>(null))
        Mockito.`when`(settingsRepository.observeTheme())
            .thenReturn(MutableStateFlow("system"))
        Mockito.`when`(settingsRepository.observeNotificationsEnabled())
            .thenReturn(MutableStateFlow(false))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_initiallySignedOut() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

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
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
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
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
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
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { it.user != null }

        viewModel.signOut()

        viewModel.uiState.first { it.user == null && !it.isSigningIn }
        val state = viewModel.uiState.value
        assertNull(state.user)
    }

    @Test
    fun exportBackup_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson()).thenReturn("{\"schemaVersion\":1}")
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("berhasil") == true)
    }

    @Test
    fun exportBackup_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson())
            .thenThrow(RuntimeException("disk full"))
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("gagal") == true)
        assertTrue(state.backupMessage?.contains("disk full") == true)
    }

    @Test
    fun onThemeChange_persists() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.onThemeChange("dark")
        testScheduler.advanceUntilIdle()

        Mockito.verify(settingsRepository).setTheme("dark")
    }

    @Test
    fun onNotificationsChange_persists() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.onNotificationsChange(true)
        testScheduler.advanceUntilIdle()

        Mockito.verify(settingsRepository).setNotificationsEnabled(true)
    }

    @Test
    fun importFromUri_success_setsSuccessMessage() = runTest(testDispatcher) {
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(backupRepository.importFromUri(activityContext, uri)).thenReturn(null)
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.importFromUri(activityContext, uri)

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("berhasil") == true)
        assertTrue(state.backupMessage?.contains("file") == true)
    }

    @Test
    fun importFromUri_failure_setsErrorMessage() = runTest(testDispatcher) {
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(backupRepository.importFromUri(activityContext, uri))
            .thenReturn("File tidak dapat dibaca")
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.importFromUri(activityContext, uri)

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("gagal") == true)
        assertTrue(state.backupMessage?.contains("File tidak dapat dibaca") == true)
    }

    @Test
    fun requestDriveConsent_alreadyAuthorized_callsActionDirectly() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.prepareDriveConsent(activityContext)).thenReturn(null)
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        var actionInvoked = false
        var senderResult: Any? = "sentinel"
        viewModel.requestDriveConsent(activityContext) { sender ->
            senderResult = sender
            if (sender == null) actionInvoked = true
        }
        testScheduler.advanceUntilIdle()

        assertTrue(actionInvoked)
        assertNull(senderResult)
    }

    @Test
    fun requestDriveConsent_needed_returnsIntentSender() = runTest(testDispatcher) {
        val intentSender = Mockito.mock(IntentSender::class.java)
        Mockito.`when`(backupRepository.prepareDriveConsent(activityContext))
            .thenReturn(intentSender)
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        var captured: IntentSender? = null
        viewModel.requestDriveConsent(activityContext) { sender -> captured = sender }
        testScheduler.advanceUntilIdle()

        assertEquals(intentSender, captured)
    }

    @Test
    fun onDriveConsentCanceled_setsMessage() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.onDriveConsentCanceled()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("dibatalkan") == true)
    }

    @Test
    fun resetRecords_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetRecordsUseCase.invoke()).thenReturn(AppResult.Success(Unit))
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.resetRecords()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("berhasil") == true)
        assertTrue(state.backupMessage?.contains("reset") == true)
    }

    @Test
    fun resetRecords_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetRecordsUseCase.invoke())
            .thenReturn(AppResult.Error("Gagal reset ringkasan: db locked"))
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.resetRecords()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("db locked") == true)
    }

    @Test
    fun resetAllData_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetAllDataUseCase.invoke()).thenReturn(AppResult.Success(Unit))
        val viewModel = ProfileViewModel(
            authRepository,
            backupRepository,
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.uiState.first { !it.isLoading }

        viewModel.resetAllData()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.backupMessage != null }
        assertTrue(state.backupMessage?.contains("berhasil") == true)
    }
}
