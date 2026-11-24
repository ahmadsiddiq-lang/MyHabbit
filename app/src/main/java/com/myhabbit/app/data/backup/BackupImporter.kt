package com.myhabbit.app.data.backup

import androidx.room.withTransaction
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import com.myhabbit.app.data.local.entity.SettingsEntity
import com.myhabbit.app.data.local.entity.UserEntity
import javax.inject.Inject

/**
 * Import backup: validasi dulu, baru replace data
 * dalam SATU transaction (ARCHITECTURE.md §16.4 point 8,
 * PRD.md §34 — data tidak boleh corrupt bila gagal).
 */
class BackupImporter @Inject constructor(
    private val database: MyHabbitDatabase,
    private val serializer: BackupSerializer,
    private val validator: BackupValidator
) {

    suspend fun import(jsonString: String): BackupValidation {
        val data = serializer.deserialize(jsonString)
            ?: return BackupValidation.Invalid("File backup bukan JSON MyHabbit yang valid")

        val validation = validator.validate(data)
        if (validation !is BackupValidation.Valid) return validation

        // Normalisasi enum ke uppercase internal (PRD §38 memakai lowercase)
        database.withTransaction {
            database.habitRecordDao().deleteAllRecords()
            database.habitDao().deleteAllHabits()
            database.userDao().deleteAllUsers()

            data.habits.forEach { habit ->
                database.habitDao().insertHabit(
                    HabitEntity(
                        id = habit.id,
                        title = habit.title,
                        category = habit.category.uppercase(),
                        description = habit.description,
                        isActive = habit.isActive,
                        createdAt = habit.createdAt,
                        updatedAt = habit.updatedAt,
                        deletedAt = habit.deletedAt
                    )
                )
            }

            data.records.forEach { record ->
                database.habitRecordDao().insertRecord(
                    HabitRecordEntity(
                        id = record.id,
                        habitId = record.habitId,
                        periodType = record.periodType.uppercase(),
                        periodKey = record.periodKey,
                        status = record.status.uppercase(),
                        createdAt = record.createdAt,
                        updatedAt = record.updatedAt
                    )
                )
            }

            data.user?.let { user ->
                database.userDao().insertUser(
                    UserEntity(
                        id = user.id,
                        googleId = user.id,
                        name = user.name,
                        email = user.email,
                        photoUrl = user.photoUrl,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }

            data.settings?.let { settings ->
                // lastBackupAt jangan mundur saat restore: gabungkan dengan
                // nilai lokal — ambil yang terbaru dari keduanya. Backup
                // berisi lastBackupAt = null bila diambil sebelum backup
                // pertama selesai, sehingga tanpa merge label "Last backup"
                // hilang setelah restore (bug ditemukan saat retest end-to-end).
                val existing = database.settingsDao().getSettings()
                val mergedLastBackupAt = maxOf(
                    existing?.lastBackupAt ?: 0L,
                    settings.lastBackupAt ?: 0L
                ).takeIf { it > 0L }
                database.settingsDao().insertSettings(
                    SettingsEntity(
                        id = 1,
                        theme = settings.theme,
                        notificationsEnabled = settings.notificationsEnabled,
                        lastBackupAt = mergedLastBackupAt
                    )
                )
            }
        }

        return BackupValidation.Valid
    }
}
