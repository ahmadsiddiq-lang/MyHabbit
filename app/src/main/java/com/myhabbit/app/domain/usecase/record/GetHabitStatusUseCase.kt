package com.myhabbit.app.domain.usecase.record

import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.repository.HabitRecordRepository
import com.myhabbit.app.domain.service.PeriodCalculator
import java.time.LocalDate
import javax.inject.Inject

/**
 * Status display habit pada periode aktif.
 *
 * Jika belum ada record untuk periode ini, hasilnya PENDING
 * tanpa menulis apa pun ke database (ARCHITECTURE.md §8.5).
 */
class GetHabitStatusUseCase @Inject constructor(
    private val habitRecordRepository: HabitRecordRepository,
    private val periodCalculator: PeriodCalculator
) {

    suspend operator fun invoke(
        habitId: String,
        habitCategory: HabitCategory,
        today: LocalDate = LocalDate.now()
    ): HabitStatus {
        val periodKey = periodCalculator.getCurrentPeriodKey(habitCategory, today)
        val record = habitRecordRepository.getRecord(habitId, periodKey)
        return record?.status ?: HabitStatus.PENDING
    }
}
