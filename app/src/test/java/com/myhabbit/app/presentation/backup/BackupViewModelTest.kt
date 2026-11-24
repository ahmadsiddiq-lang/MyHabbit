package com.myhabbit.app.presentation.backup

import android.content.Context
import android.content.IntentSender
import android.net.Uri
import com.myhabbit.app.domain.repository.BackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class BackupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var backupRepository: BackupRepository
    private val activityContext: Context = Mockito.mock(Context::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        backupRepository = Mockito.mock(BackupRepository::class.java)
        Mockito.`when`(backupRepository.observeLastBackupAt())
            .thenReturn(MutableStateFlow<Long?>(null))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun exportBackup_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson()).thenReturn("{\"schemaVersion\":1}")
        val viewModel = BackupViewModel(backupRepository)

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("berhasil") == true)
    }

    @Test
    fun exportBackup_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.exportToJson())
            .thenThrow(RuntimeException("disk full"))
        val viewModel = BackupViewModel(backupRepository)

        viewModel.exportBackup()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("gagal") == true)
        assertTrue(state.message?.contains("disk full") == true)
    }

    @Test
    fun importFromUri_success_setsSuccessMessage() = runTest(testDispatcher) {
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(backupRepository.importFromUri(activityContext, uri)).thenReturn(null)
        val viewModel = BackupViewModel(backupRepository)

        viewModel.importFromUri(activityContext, uri)

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("berhasil") == true)
        assertTrue(state.message?.contains("file") == true)
    }

    @Test
    fun importFromUri_failure_setsErrorMessage() = runTest(testDispatcher) {
        val uri = Mockito.mock(Uri::class.java)
        Mockito.`when`(backupRepository.importFromUri(activityContext, uri))
            .thenReturn("File tidak dapat dibaca")
        val viewModel = BackupViewModel(backupRepository)

        viewModel.importFromUri(activityContext, uri)

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("gagal") == true)
        assertTrue(state.message?.contains("File tidak dapat dibaca") == true)
    }

    @Test
    fun requestDriveConsent_alreadyAuthorized_callsActionDirectly() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.prepareDriveConsent(activityContext)).thenReturn(null)
        val viewModel = BackupViewModel(backupRepository)

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
        val viewModel = BackupViewModel(backupRepository)

        var captured: IntentSender? = null
        viewModel.requestDriveConsent(activityContext) { sender -> captured = sender }
        testScheduler.advanceUntilIdle()

        assertEquals(intentSender, captured)
    }

    @Test
    fun onDriveConsentCanceled_setsMessage() = runTest(testDispatcher) {
        val viewModel = BackupViewModel(backupRepository)

        viewModel.onDriveConsentCanceled()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("dibatalkan") == true)
    }

    @Test
    fun backupToDrive_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.backupToDrive(activityContext)).thenReturn(null)
        val viewModel = BackupViewModel(backupRepository)

        viewModel.backupToDrive(activityContext)

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("berhasil") == true)
    }

    @Test
    fun restoreFromDrive_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(backupRepository.restoreFromDrive(activityContext))
            .thenReturn("Belum ada backup")
        val viewModel = BackupViewModel(backupRepository)

        viewModel.restoreFromDrive(activityContext)

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("gagal") == true)
    }

    @Test
    fun lastBackupAt_propagatesFromRepository() = runTest(testDispatcher) {
        val flow = MutableStateFlow<Long?>(null)
        Mockito.`when`(backupRepository.observeLastBackupAt()).thenReturn(flow)
        val viewModel = BackupViewModel(backupRepository)

        flow.value = 1731800000000L

        val state = viewModel.uiState.first { it.lastBackupAt != null }
        assertEquals(1731800000000L, state.lastBackupAt)
    }
}
