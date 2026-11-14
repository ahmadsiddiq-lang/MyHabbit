package com.myhabbit.app.domain.model

data class Habit(
    val id: String,
    val title: String,
    val category: HabitCategory,
    val description: String?,
    val isActive: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Long?
)
