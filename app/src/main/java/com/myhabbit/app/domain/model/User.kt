package com.myhabbit.app.domain.model

data class User(
    val id: String,
    val googleId: String?,
    val name: String,
    val email: String,
    val photoUrl: String?
)
