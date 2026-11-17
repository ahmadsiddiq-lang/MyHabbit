package com.myhabbit.app.domain.repository

import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import kotlinx.coroutines.flow.Flow

interface HabitRecordRepository {

    fun observeRecordsByHabit(habitId: String): Flow<List<HabitRecord>>

    suspend fun getRecord(habitId: String, periodKey: String): HabitRecord?

    suspend fun getRecordsByHabit(habitId: String): List<HabitRecord>

    suspend fun getRecordsByPeriod(periodKey: String): List<HabitRecord>

    suspend fun saveRecord(record: HabitRecord)

    suspend fun updateStatus(
        habitId: String,
        periodKey: String,
        status: HabitStatus,
        updatedAt: Long
    )

    /** Hard reset: hapus SEMUA records (PRD.md — Reset Data). */
    suspend fun deleteAllRecords()
}
