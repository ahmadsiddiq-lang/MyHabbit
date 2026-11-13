package com.myhabbit.app.domain.repository

/**
 * Repository backup (ARCHITECTURE.md §16):
 * generate, validasi, export, import.
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
}
