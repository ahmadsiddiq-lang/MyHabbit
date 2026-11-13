package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {

    operator fun invoke(): Flow<List<Habit>> = habitRepository.observeActiveHabits()
}
