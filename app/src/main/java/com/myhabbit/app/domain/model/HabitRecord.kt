package com.myhabbit.app.domain.model

data class HabitRecord(
    val id: String,
    val habitId: String,
    val periodType: HabitCategory,
    val periodKey: String,
    val status: HabitStatus,
    val createdAt: Long,
    val updatedAt: Long
)
