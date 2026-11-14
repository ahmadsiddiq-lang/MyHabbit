package com.myhabbit.app.data.backup

import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.dao.UserDao
import javax.inject.Inject

/**
 * Generate backup JSON dari database lokal (ARCHITECTURE.md §16.3).
 * Termasuk habit aktif + soft-deleted, semua record, profil user,
 * dan settings — tanpa token/secrets.
 */
class BackupExporter @Inject constructor(
    private val habitDao: HabitDao,
    private val habitRecordDao: HabitRecordDao,
    private val userDao: UserDao,
    private val settingsDao: SettingsDao
) {

    suspend fun export(): BackupData {
        val habits = habitDao.getAllHabits().map { entity ->
            HabitJson(
                id = entity.id,
                title = entity.title,
                category = entity.category,
                description = entity.description,
                isActive = entity.isActive,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                deletedAt = entity.deletedAt
            )
        }

        val records = habitRecordDao.getAllRecords().map { entity ->
            RecordJson(
                id = entity.id,
                habitId = entity.habitId,
                periodType = entity.periodType,
                periodKey = entity.periodKey,
                status = entity.status,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }

        val user = userDao.getCurrentUser()?.let { entity ->
            UserJson(
                id = entity.id,
                name = entity.name,
                email = entity.email,
                photoUrl = entity.photoUrl
            )
        }

        val settings = settingsDao.getSettings()?.let { entity ->
            SettingsJson(
                theme = entity.theme,
                notificationsEnabled = entity.notificationsEnabled,
                lastBackupAt = entity.lastBackupAt
            )
        }

        return BackupData(
            schemaVersion = BACKUP_SCHEMA_VERSION,
            app = AppJson(name = "MyHabbit", version = "1.0.0"),
            exportedAt = System.currentTimeMillis(),
            user = user,
            habits = habits,
            records = records,
            settings = settings
        )
    }
}
