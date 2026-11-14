package com.myhabbit.app.domain.usecase.record

import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitStatus
import javax.inject.Inject

/**
 * Status habit beserta key periode aktifnya (untuk dashboard).
 */
data class HabitWithStatus(
    val habit: Habit,
    val status: HabitStatus,
    val periodKey: String
)
