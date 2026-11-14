package com.myhabbit.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Pengaturan aplikasi (PRD.md §49: theme default = system;
 * §48: notification toggle).
 */
interface SettingsRepository {

    /** "system" | "light" | "dark" */
    fun observeTheme(): Flow<String>

    suspend fun setTheme(theme: String)

    fun observeNotificationsEnabled(): Flow<Boolean>

    suspend fun setNotificationsEnabled(enabled: Boolean)
}
