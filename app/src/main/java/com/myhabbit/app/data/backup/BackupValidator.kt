package com.myhabbit.app.data.backup

import javax.inject.Inject

/**
 * Validasi backup sebelum import (ARCHITECTURE.md §16.4):
 * 1. schemaVersion didukung (reject versi masa depan)
 * 2. Field wajib tidak kosong
 * 3. ID unik
 * 4. record.habitId harus merujuk habit yang ada
 * 5. Nilai enum valid (status & category, PRD.md §38)
 */
class BackupValidator @Inject constructor() {

    private val validCategories = setOf("DAILY", "WEEKLY", "MONTHLY")
    private val validStatuses = setOf("PENDING", "COMPLETED", "FAILED")

    fun validate(data: BackupData): BackupValidation {
        // 1. Schema version
        if (data.schemaVersion > BACKUP_SCHEMA_VERSION) {
            return BackupValidation.Invalid(
                "Versi backup (${data.schemaVersion}) lebih baru dari versi aplikasi ($BACKUP_SCHEMA_VERSION). Perbarui aplikasi."
            )
        }
        if (data.schemaVersion < 1) {
            return BackupValidation.Invalid("schemaVersion tidak valid: ${data.schemaVersion}")
        }

        // 2. Required fields — habit
        data.habits.forEach { habit ->
            if (habit.id.isBlank()) return BackupValidation.Invalid("Ada habit dengan id kosong")
            if (habit.title.isBlank()) return BackupValidation.Invalid("Habit '${habit.id}' tidak punya judul")
            if (habit.category.uppercase() !in validCategories) {
                return BackupValidation.Invalid("Habit '${habit.id}' punya kategori tidak valid: ${habit.category}")
            }
        }

        // 2. Required fields — record + 3. unique IDs + 5. enum
        val recordIds = mutableSetOf<String>()
        data.records.forEach { record ->
            if (record.id.isBlank()) return BackupValidation.Invalid("Ada record dengan id kosong")
            if (record.habitId.isBlank()) return BackupValidation.Invalid("Record '${record.id}' tidak punya habitId")
            if (!recordIds.add(record.id)) {
                return BackupValidation.Invalid("Record id duplikat: ${record.id}")
            }
            if (record.periodKey.isBlank()) return BackupValidation.Invalid("Record '${record.id}' tidak punya periodKey")
            if (record.status.uppercase() !in validStatuses) {
                return BackupValidation.Invalid("Record '${record.id}' punya status tidak valid: ${record.status}")
            }
            if (record.periodType.uppercase() !in validCategories) {
                return BackupValidation.Invalid("Record '${record.id}' punya periodType tidak valid: ${record.periodType}")
            }
        }

        // 3. Unique habit IDs
        val habitIds = mutableSetOf<String>()
        data.habits.forEach { habit ->
            if (!habitIds.add(habit.id)) {
                return BackupValidation.Invalid("Habit id duplikat: ${habit.id}")
            }
        }

        // 4. Record harus merujuk habit yang ada
        data.records.forEach { record ->
            if (record.habitId !in habitIds) {
                return BackupValidation.Invalid(
                    "Record '${record.id}' merujuk habit '${record.habitId}' yang tidak ada di backup"
                )
            }
        }

        return BackupValidation.Valid
    }
}
