package com.myhabbit.app.domain.usecase.summary

import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import javax.inject.Inject

/**
 * Completion rate per habit untuk bar chart.
 *
 * Habit tanpa record evaluated tidak dimasukkan agar chart
 * hanya menampilkan aktivitas yang relevan.
 */
class GetHabitStatisticsUseCase @Inject constructor(
    private val habitDao: HabitDao,
    private val habitRecordDao: HabitRecordDao
) {

    suspend operator fun invoke(
        category: HabitCategory? = null
    ): List<HabitStatistic> {
        val habits = habitDao.getAllHabits()
        val records = habitRecordDao.getAllRecords().map { it.toDomain() }

        return habits
            .filter { category == null || it.category == category.name }
            .map { habit ->
                val habitRecords = records.filter { it.habitId == habit.id }
                val completed = habitRecords.count { it.status == HabitStatus.COMPLETED }
                val failed = habitRecords.count { it.status == HabitStatus.FAILED }
                val evaluated = completed + failed
                HabitStatistic(
                    habitId = habit.id,
                    habitTitle = habit.title,
                    category = habit.category,
                    completed = completed,
                    failed = failed,
                    completionRate = if (evaluated == 0) 0.0 else completed.toDouble() / evaluated * 100.0
                )
            }
            .filter { it.completed + it.failed > 0 }
            .sortedByDescending { it.completionRate }
    }
}
