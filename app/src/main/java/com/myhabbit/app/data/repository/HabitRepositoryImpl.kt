package com.myhabbit.app.data.repository

import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.data.mapper.toEntity
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun observeActiveHabits(): Flow<List<Habit>> =
        habitDao.observeActiveHabits().map { habits -> habits.map { it.toDomain() } }

    override suspend fun getActiveHabits(): List<Habit> =
        habitDao.getActiveHabits().map { it.toDomain() }

    override suspend fun getHabitById(id: String): Habit? =
        habitDao.getHabitById(id)?.toDomain()

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit.toEntity())
    }

    override suspend fun softDeleteHabit(id: String, deletedAt: Long, updatedAt: Long) {
        habitDao.softDeleteHabit(id, deletedAt, updatedAt)
    }

    override suspend fun deleteAllHabits() {
        habitDao.deleteAllHabits()
    }
}
