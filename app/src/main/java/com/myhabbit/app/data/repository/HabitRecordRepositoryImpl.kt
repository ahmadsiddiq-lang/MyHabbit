package com.myhabbit.app.data.repository

import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.data.mapper.toEntity
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.repository.HabitRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRecordRepositoryImpl @Inject constructor(
    private val habitRecordDao: HabitRecordDao
) : HabitRecordRepository {

    override fun observeRecordsByHabit(habitId: String): Flow<List<HabitRecord>> =
        habitRecordDao.observeRecordsByHabit(habitId)
            .map { records -> records.map { it.toDomain() } }

    override suspend fun getRecord(habitId: String, periodKey: String): HabitRecord? =
        habitRecordDao.getRecord(habitId, periodKey)?.toDomain()

    override suspend fun getRecordsByHabit(habitId: String): List<HabitRecord> =
        habitRecordDao.getRecordsByHabit(habitId).map { it.toDomain() }

    override suspend fun getRecordsByPeriod(periodKey: String): List<HabitRecord> =
        habitRecordDao.getRecordsByPeriod(periodKey).map { it.toDomain() }

    override suspend fun saveRecord(record: HabitRecord) {
        val entity = record.toEntity()
        if (habitRecordDao.countRecordById(entity.id) > 0) {
            habitRecordDao.updateRecord(entity)
        } else {
            habitRecordDao.insertRecord(entity)
        }
    }

    override suspend fun updateStatus(
        habitId: String,
        periodKey: String,
        status: HabitStatus,
        updatedAt: Long
    ) {
        val existing = habitRecordDao.getRecord(habitId, periodKey) ?: return
        habitRecordDao.updateRecord(
            existing.copy(status = status.name, updatedAt = updatedAt)
        )
    }
}
