package com.myhabbit.app.data.repository

import android.content.Context
import com.myhabbit.app.data.backup.BackupExporter
import com.myhabbit.app.data.backup.BackupImporter
import com.myhabbit.app.data.backup.BackupSerializer
import com.myhabbit.app.data.backup.BackupValidation
import com.myhabbit.app.domain.repository.BackupRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val importer: BackupImporter
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
}
