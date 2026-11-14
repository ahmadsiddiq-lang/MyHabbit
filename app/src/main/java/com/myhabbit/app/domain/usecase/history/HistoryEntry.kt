package com.myhabbit.app.domain.usecase.history

import com.myhabbit.app.domain.model.HabitStatus

/**
 * Satu baris history untuk ditampilkan (PRD.md §21).
 */
data class HistoryEntry(
    val habitId: String,
    val habitTitle: String,
    val category: String,
    val periodKey: String,
    val status: HabitStatus
)
