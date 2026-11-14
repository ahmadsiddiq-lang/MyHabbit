package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.repository.HabitRepository
import com.myhabbit.app.domain.validation.ActivityValidator
import java.util.UUID
import javax.inject.Inject

class CreateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(
        title: String,
        category: HabitCategory,
        description: String?,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): AppResult<String> {
        ActivityValidator.validateTitle(title)?.let { return AppResult.Error(it) }
        ActivityValidator.validateDescription(description)?.let { return AppResult.Error(it) }

        val habit = Habit(
            id = "habit_${UUID.randomUUID()}",
            title = title.trim(),
            category = category,
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            isActive = true,
            createdAt = currentTimeMillis,
            updatedAt = currentTimeMillis,
            deletedAt = null
        )

        return try {
            habitRepository.insertHabit(habit)
            AppResult.Success(habit.id)
        } catch (e: Exception) {
            AppResult.Error("Gagal menyimpan activity: ${e.message}")
        }
    }
}
