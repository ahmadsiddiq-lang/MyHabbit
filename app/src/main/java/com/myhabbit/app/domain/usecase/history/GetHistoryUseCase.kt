package com.myhabbit.app.domain.usecase.history

import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.domain.model.HabitStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Mengambil seluruh history record beserta judul habit-nya,
 * reaktif terhadap perubahan database.
 */
class GetHistoryUseCase @Inject constructor(
    private val habitDao: HabitDao,
    private val habitRecordDao: HabitRecordDao
) {

    fun observe(): Flow<List<HistoryEntry>> = combine(
        habitDao.getAllHabitsAsFlow(),
        habitRecordDao.observeAllRecords()
    ) { habits, records ->
        val titleById = habits.associate { it.id to it.title }
        val categoryById = habits.associate { it.id to it.category }
        records.map { record ->
            HistoryEntry(
                habitId = record.habitId,
                habitTitle = titleById[record.habitId] ?: record.habitId,
                category = categoryById[record.habitId] ?: record.periodType,
                periodKey = record.periodKey,
                status = HabitStatus.valueOf(record.status)
            )
        }.sortedByDescending { it.periodKey }
    }
}
