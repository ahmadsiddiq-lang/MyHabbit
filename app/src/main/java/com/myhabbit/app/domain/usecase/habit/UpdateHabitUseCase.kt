package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.repository.HabitRepository
import com.myhabbit.app.domain.validation.ActivityValidator
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(
        habitId: String,
        title: String,
        category: HabitCategory,
        description: String?,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): AppResult<Unit> {
        val existing = habitRepository.getHabitById(habitId)
            ?: return AppResult.Error("Activity tidak ditemukan")

        ActivityValidator.validateTitle(title)?.let { return AppResult.Error(it) }
        ActivityValidator.validateDescription(description)?.let { return AppResult.Error(it) }

        // ID tidak berubah saat edit. History lama tetap utuh.
        val updated = existing.copy(
            title = title.trim(),
            category = category,
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            updatedAt = currentTimeMillis
        )

        return try {
            habitRepository.updateHabit(updated)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error("Gagal memperbarui activity: ${e.message}")
        }
    }
}
