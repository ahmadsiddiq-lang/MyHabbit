package com.myhabbit.app.domain.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

/**
 * Repository backup (ARCHITECTURE.md §15, §16):
 * generate, validasi, export, import + Drive (§15).
 */
interface BackupRepository {

    /**
     * Generate backup JSON dari database.
     * @return JSON string siap disimpan/upload.
     */
    suspend fun exportToJson(): String

    /**
     * Import backup: validasi lalu replace database.
     * @return null jika sukses, atau pesan error validasi.
     */
    suspend fun importFromJson(jsonString: String): String?

    /**
     * Baca isi file backup lokal; null bila belum ada.
     */
    fun readBackupFile(): String?

    /**
     * Import dari file yang dipilih user via SAF.
     * @return null jika sukses, atau pesan error.
     */
    suspend fun importFromUri(activityContext: Context, uri: Uri): String?

    /**
     * Backup ke Google Drive: export → validasi → upload →
     * update lastBackupAt (ARCHITECTURE.md §15.2).
     * @return null jika sukses, atau pesan error.
     */
    suspend fun backupToDrive(activityContext: Context): String?

    /**
     * Restore dari Drive: download → validasi → replace database.
     * @return null jika sukses, atau pesan error (termasuk "belum ada backup").
     */
    suspend fun restoreFromDrive(activityContext: Context): String?

    /** Waktu backup Drive terakhir (epoch millis), null bila belum pernah. */
    fun observeLastBackupAt(): Flow<Long?>
}
