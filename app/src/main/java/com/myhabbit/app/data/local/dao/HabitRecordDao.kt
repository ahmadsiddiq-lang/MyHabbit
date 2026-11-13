package com.myhabbit.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitRecordDao {

    @Query("SELECT * FROM habit_records WHERE habitId = :habitId ORDER BY periodKey DESC")
    fun observeRecordsByHabit(habitId: String): Flow<List<HabitRecordEntity>>

    @Query("SELECT * FROM habit_records WHERE habitId = :habitId ORDER BY periodKey DESC")
    suspend fun getRecordsByHabit(habitId: String): List<HabitRecordEntity>

    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND periodKey = :periodKey")
    suspend fun getRecord(habitId: String, periodKey: String): HabitRecordEntity?

    @Query("SELECT * FROM habit_records WHERE periodKey = :periodKey")
    suspend fun getRecordsByPeriod(periodKey: String): List<HabitRecordEntity>

    @Query("SELECT * FROM habit_records ORDER BY periodKey DESC")
    suspend fun getAllRecords(): List<HabitRecordEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRecord(record: HabitRecordEntity)

    @Update
    suspend fun updateRecord(record: HabitRecordEntity)

    @Query("SELECT COUNT(*) FROM habit_records WHERE id = :id")
    suspend fun countRecordById(id: String): Int

    @Query("DELETE FROM habit_records WHERE id = :id")
    suspend fun deleteRecord(id: String)
}
