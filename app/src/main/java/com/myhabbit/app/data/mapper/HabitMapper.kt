package com.myhabbit.app.data.mapper

import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    title = title,
    category = HabitCategory.valueOf(category),
    description = description,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    title = title,
    category = category.name,
    description = description,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)
