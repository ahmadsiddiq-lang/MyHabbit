package com.myhabbit.app.domain.model

data class Summary(
    val total: Int,
    val completed: Int,
    val failed: Int,
    val pending: Int,
    val evaluated: Int,
    val completionRate: Double
)
