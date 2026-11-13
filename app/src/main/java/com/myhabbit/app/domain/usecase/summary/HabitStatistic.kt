package com.myhabbit.app.domain.usecase.summary

/**
 * Data statistik satu habit untuk bar chart (PRD.md §28.1).
 */
data class HabitStatistic(
    val habitId: String,
    val habitTitle: String,
    val category: String,
    val completed: Int,
    val failed: Int,
    val completionRate: Double
)
