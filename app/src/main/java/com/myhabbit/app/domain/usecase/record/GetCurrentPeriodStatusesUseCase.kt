package com.myhabbit.app.domain.usecase.record

import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.repository.HabitRecordRepository
import com.myhabbit.app.domain.service.PeriodCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Status semua habit aktif untuk periode masing-masing, reaktif terhadap
 * perubahan record (ARCHITECTURE.md §11 GetCurrentPeriodRecordsUseCase).
 */
class GetCurrentPeriodStatusesUseCase @Inject constructor(
    private val habitRecordRepository: HabitRecordRepository,
    private val periodCalculator: PeriodCalculator
) {

    fun observe(
        habitsFlow: Flow<List<Habit>>,
        today: LocalDate = LocalDate.now()
    ): Flow<List<HabitWithStatus>> = habitsFlow.map { habits ->
        habits.map { habit ->
            val periodKey = periodCalculator.getCurrentPeriodKey(habit.category, today)
            val record = habitRecordRepository.getRecord(habit.id, periodKey)
            HabitWithStatus(
                habit = habit,
                status = record?.status ?: HabitStatus.PENDING,
                periodKey = periodKey
            )
        }
    }

    suspend operator fun invoke(
        habits: List<Habit>,
        today: LocalDate = LocalDate.now()
    ): List<HabitWithStatus> = habits.map { habit ->
        val periodKey = periodCalculator.getCurrentPeriodKey(habit.category, today)
        val record = habitRecordRepository.getRecord(habit.id, periodKey)
        HabitWithStatus(
            habit = habit,
            status = record?.status ?: HabitStatus.PENDING,
            periodKey = periodKey
        )
    }
}
