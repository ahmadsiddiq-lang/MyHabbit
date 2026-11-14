package com.myhabbit.app.data.repository

import android.content.Context
import com.myhabbit.app.data.backup.BackupExporter
import com.myhabbit.app.data.backup.BackupImporter
import com.myhabbit.app.data.backup.BackupSerializer
import com.myhabbit.app.data.backup.BackupValidation
import com.myhabbit.app.data.backup.BackupValidator
import com.myhabbit.app.data.drive.GoogleDriveManager
import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.entity.SettingsEntity
import com.myhabbit.app.domain.repository.BackupRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

/**
 * Export/import backup file ke storage app-specific
 * (`files/backup/`) — tanpa permission. Drive di Phase 12.
 */
class BackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exporter: BackupExporter,
    private val serializer: BackupSerializer,
    private val importer: BackupImporter,
    private val validator: BackupValidator,
    private val driveManager: GoogleDriveManager,
    private val settingsDao: SettingsDao
) : BackupRepository {

    private fun backupDir(): File =
        File(context.filesDir, "backup").apply { mkdirs() }

    private fun backupFile(): File = File(backupDir(), "myhabbit-backup.json")

    override suspend fun exportToJson(): String {
        val data = exporter.export()
        val json = serializer.serialize(data)
        backupFile().writeText(json)
        return json
    }

    override suspend fun importFromJson(jsonString: String): String? {
        return when (val result = importer.import(jsonString)) {
            is BackupValidation.Valid -> null
            is BackupValidation.Invalid -> result.reason
        }
    }

    override fun readBackupFile(): String? {
        val file = backupFile()
        return if (file.exists()) file.readText() else null
    }

    override suspend fun backupToDrive(activityContext: Context): String? {
        // §15.2: export → validasi → upload → update lastBackupAt
        val data = exporter.export()
        val json = serializer.serialize(data)

        // Validasi sebelum upload
        if (validator.validate(data) is BackupValidation.Invalid) {
            return "Data backup tidak valid — export gagal"
        }

        val upload = driveManager.uploadBackup(activityContext, json)
        val error = upload.exceptionOrNull()
        return if (error != null) {
            error.message
        } else {
            updateLastBackupAt(System.currentTimeMillis())
            null
        }
    }

    override suspend fun restoreFromDrive(activityContext: Context): String? {
        val download = driveManager.downloadBackup(activityContext)
        download.exceptionOrNull()?.let { return it.message }
        val content = download.getOrNull() ?: return "Belum ada backup di Google Drive"

        return importFromJson(content)
    }

    override fun observeLastBackupAt(): Flow<Long?> {
        return settingsDao.observeSettings().map { it?.lastBackupAt }
    }

    private suspend fun updateLastBackupAt(time: Long) {
        val existing = settingsDao.getSettings()
        if (existing != null) {
            settingsDao.updateLastBackupAt(time)
        } else {
            settingsDao.insertSettings(
                SettingsEntity(id = 1, theme = "system", notificationsEnabled = false, lastBackupAt = time)
            )
        }
    }
}
