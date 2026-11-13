package com.myhabbit.app.data.backup

import kotlinx.serialization.Serializable

/**
 * Schema backup v1 (PRD.md §35, ARCHITECTURE.md §16.1).
 *
 * Timestamp memakai epoch millis (Long) — model internal
 * juga epoch millis, hindari konversi ganda.
 * Enum lowercase: pending/completed, daily/weekly/monthly (PRD.md §38).
 */
const val BACKUP_SCHEMA_VERSION = 1

@Serializable
data class BackupData(
    val schemaVersion: Int,
    val app: AppJson,
    val exportedAt: Long,
    val user: UserJson? = null,
    val habits: List<HabitJson> = emptyList(),
    val records: List<RecordJson> = emptyList(),
    val settings: SettingsJson? = null
)

@Serializable
data class AppJson(
    val name: String,
    val version: String
)

@Serializable
data class UserJson(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String? = null
)

@Serializable
data class HabitJson(
    val id: String,
    val title: String,
    val category: String,
    val description: String? = null,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long? = null
)

@Serializable
data class RecordJson(
    val id: String,
    val habitId: String,
    val periodType: String,
    val periodKey: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class SettingsJson(
    val theme: String,
    val notificationsEnabled: Boolean,
    val lastBackupAt: Long? = null
)

/** Hasil validasi import. */
sealed class BackupValidation {
    data object Valid : BackupValidation()
    data class Invalid(val reason: String) : BackupValidation()
}
