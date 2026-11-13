package com.myhabbit.app.data.mapper

import com.myhabbit.app.data.local.entity.HabitRecordEntity
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus

fun HabitRecordEntity.toDomain(): HabitRecord = HabitRecord(
    id = id,
    habitId = habitId,
    periodType = HabitCategory.valueOf(periodType),
    periodKey = periodKey,
    status = HabitStatus.valueOf(status),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun HabitRecord.toEntity(): HabitRecordEntity = HabitRecordEntity(
    id = id,
    habitId = habitId,
    periodType = periodType.name,
    periodKey = periodKey,
    status = status.name,
    createdAt = createdAt,
    updatedAt = updatedAt
)
