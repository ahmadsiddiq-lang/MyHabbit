package com.myhabbit.app.presentation.settings

import com.myhabbit.app.core.result.AppResult
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var resetRecordsUseCase: ResetRecordsUseCase
    private lateinit var resetAllDataUseCase: ResetAllDataUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settingsRepository = Mockito.mock(SettingsRepository::class.java)
        resetRecordsUseCase = Mockito.mock(ResetRecordsUseCase::class.java)
        resetAllDataUseCase = Mockito.mock(ResetAllDataUseCase::class.java)
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
    fun uiState_reflectsStoredSettings() = runTest(testDispatcher) {
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        val state = viewModel.uiState.first()

        assertEquals("system", state.theme)
        assertFalse(state.notificationsEnabled)
    }

    @Test
    fun onThemeChange_persists() = runTest(testDispatcher) {
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        viewModel.onThemeChange("dark")
        testScheduler.advanceUntilIdle()

        Mockito.verify(settingsRepository).setTheme("dark")
    }

    @Test
    fun onNotificationsChange_persists() = runTest(testDispatcher) {
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        viewModel.onNotificationsChange(true)
        testScheduler.advanceUntilIdle()

        Mockito.verify(settingsRepository).setNotificationsEnabled(true)
    }

    @Test
    fun resetRecords_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetRecordsUseCase.invoke()).thenReturn(AppResult.Success(Unit))
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        viewModel.resetRecords()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("berhasil") == true)
        assertTrue(state.message?.contains("reset") == true)
    }

    @Test
    fun resetRecords_failure_setsErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetRecordsUseCase.invoke())
            .thenReturn(AppResult.Error("Gagal reset ringkasan: db locked"))
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        viewModel.resetRecords()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("db locked") == true)
    }

    @Test
    fun resetAllData_success_setsMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetAllDataUseCase.invoke()).thenReturn(AppResult.Success(Unit))
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )

        viewModel.resetAllData()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.first { it.message != null }
        assertTrue(state.message?.contains("berhasil") == true)
    }

    @Test
    fun clearMessage_clearsMessage() = runTest(testDispatcher) {
        Mockito.`when`(resetAllDataUseCase.invoke()).thenReturn(AppResult.Success(Unit))
        val viewModel = SettingsViewModel(
            settingsRepository,
            resetRecordsUseCase,
            resetAllDataUseCase
        )
        viewModel.resetAllData()
        viewModel.uiState.first { it.message != null }

        viewModel.clearMessage()

        viewModel.uiState.first { it.message == null }
    }
}
