package com.myhabbit.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_records",
    indices = [
        Index(value = ["habitId"]),
        Index(value = ["habitId", "periodKey"], unique = true)
    ]
)
data class HabitRecordEntity(
    @PrimaryKey
    val id: String,
    val habitId: String,
    val periodType: String,
    val periodKey: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)
