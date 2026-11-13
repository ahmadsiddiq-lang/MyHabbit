package com.myhabbit.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myhabbit.app.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits WHERE deletedAt IS NULL ORDER BY createdAt ASC")
    fun observeActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE deletedAt IS NULL ORDER BY createdAt ASC")
    suspend fun getActiveHabits(): List<HabitEntity>

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabitsAsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("UPDATE habits SET deletedAt = :deletedAt, isActive = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteHabit(id: String, deletedAt: Long, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM habits WHERE id = :id")
    suspend fun countHabitById(id: String): Int
}
