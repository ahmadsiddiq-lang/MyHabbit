package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(
        habitId: String,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): AppResult<Unit> {
        val existing = habitRepository.getHabitById(habitId)
            ?: return AppResult.Error("Activity tidak ditemukan")

        return try {
            // Soft delete: history tidak boleh hilang (PRD.md §16).
            habitRepository.softDeleteHabit(
                id = habitId,
                deletedAt = currentTimeMillis,
                updatedAt = currentTimeMillis
            )
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error("Gagal menghapus activity: ${e.message}")
        }
    }
}
