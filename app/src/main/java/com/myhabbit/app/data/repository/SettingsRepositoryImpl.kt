package com.myhabbit.app.data.repository

import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.entity.SettingsEntity
import com.myhabbit.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun observeTheme(): Flow<String> {
        return settingsDao.observeSettings().map { it?.theme ?: "system" }
    }

    override suspend fun setTheme(theme: String) {
        ensureSettingsRow { copy(theme = theme) }
    }

    override fun observeNotificationsEnabled(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.notificationsEnabled ?: false }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        ensureSettingsRow { copy(notificationsEnabled = enabled) }
    }

    private suspend inline fun ensureSettingsRow(
        update: SettingsEntity.() -> SettingsEntity
    ) {
        val existing = settingsDao.getSettings()
        if (existing != null) {
            settingsDao.insertSettings(existing.update())
        } else {
            settingsDao.insertSettings(
                SettingsEntity(id = 1, theme = "system", notificationsEnabled = false, lastBackupAt = null)
                    .update()
            )
        }
    }
}
