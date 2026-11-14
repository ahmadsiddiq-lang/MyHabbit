package com.myhabbit.app.domain.repository

import com.myhabbit.app.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun observeActiveHabits(): Flow<List<Habit>>

    suspend fun getActiveHabits(): List<Habit>

    suspend fun getHabitById(id: String): Habit?

    suspend fun insertHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    suspend fun softDeleteHabit(id: String, deletedAt: Long, updatedAt: Long)
}
