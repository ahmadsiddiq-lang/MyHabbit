package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.repository.HabitRecordRepository
import javax.inject.Inject

/**
 * "Reset Ringkasan": hapus semua habit_records (history/chart/status)
 * TANPA menghapus activity. Setelah ini chart mulai dari nol dan
 * hanya mencatat status baru (permintaan user — reset data ringkasan).
 * Akun, pengaturan, dan activity tetap utuh.
 */
class ResetRecordsUseCase @Inject constructor(
    private val recordRepository: HabitRecordRepository
) {

    suspend operator fun invoke(): AppResult<Unit> {
        return try {
            recordRepository.deleteAllRecords()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error("Gagal reset ringkasan: ${e.message}")
        }
    }
}
