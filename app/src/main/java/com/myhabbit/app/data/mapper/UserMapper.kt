package com.myhabbit.app.data.mapper

import com.myhabbit.app.data.local.entity.UserEntity
import com.myhabbit.app.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    googleId = googleId,
    name = name,
    email = email,
    photoUrl = photoUrl
)
