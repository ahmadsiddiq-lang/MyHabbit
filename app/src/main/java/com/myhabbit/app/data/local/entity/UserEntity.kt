package com.myhabbit.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val googleId: String?,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)
