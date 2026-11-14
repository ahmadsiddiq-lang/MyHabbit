package com.myhabbit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int,
    val theme: String,
    val notificationsEnabled: Boolean,
    val lastBackupAt: Long?
)
