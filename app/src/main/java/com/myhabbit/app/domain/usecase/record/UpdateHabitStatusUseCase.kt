package com.myhabbit.app.domain.usecase.record

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.repository.HabitRecordRepository
import com.myhabbit.app.domain.service.PeriodCalculator
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

/**
 * Mengubah status habit untuk periode aktif.
 *
 * Aturan (ARCHITECTURE.md §8.5, §11):
 * - Jika belum ada record untuk periode ini, record baru dibuat.
 * - Jika sudah ada, record diupdate (termasuk kembali ke PENDING).
 * - Record lama pada periode lain tidak pernah disentuh.
 */
class UpdateHabitStatusUseCase @Inject constructor(
    private val habitRecordRepository: HabitRecordRepository,
    private val periodCalculator: PeriodCalculator
) {

    suspend operator fun invoke(
        habitId: String,
        habitCategory: HabitCategory,
        status: HabitStatus,
        today: LocalDate = LocalDate.now(),
        currentTimeMillis: Long = System.currentTimeMillis()
    ): AppResult<Unit> {
        val periodKey = periodCalculator.getCurrentPeriodKey(habitCategory, today)

        return try {
            val existing = habitRecordRepository.getRecord(habitId, periodKey)

            if (existing != null) {
                habitRecordRepository.updateStatus(
                    habitId = habitId,
                    periodKey = periodKey,
                    status = status,
                    updatedAt = currentTimeMillis
                )
            } else {
                habitRecordRepository.saveRecord(
                    HabitRecord(
                        id = "record_${UUID.randomUUID()}",
                        habitId = habitId,
                        periodType = habitCategory,
                        periodKey = periodKey,
                        status = status,
                        createdAt = currentTimeMillis,
                        updatedAt = currentTimeMillis
                    )
                )
            }
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error("Gagal menyimpan status: ${e.message}")
        }
    }
}
