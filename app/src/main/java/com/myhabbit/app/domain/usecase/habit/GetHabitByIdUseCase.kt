package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitByIdUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    suspend operator fun invoke(habitId: String): Habit? =
        habitRepository.getHabitById(habitId)
}
