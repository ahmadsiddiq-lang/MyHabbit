package com.myhabbit.app.domain.service

import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.model.Summary

/**
 * Perhitungan summary sesuai PRD.md §24 & §59:
 *
 * evaluated = completed + failed
 * completionRate = completed / evaluated * 100
 *
 * Pending tidak masuk denominator.
 * Jika evaluated = 0, completionRate = 0.
 */
object CompletionCalculator {

    fun calculate(records: List<HabitRecord>): Summary {
        val total = records.size
        val completed = records.count { it.status == HabitStatus.COMPLETED }
        val failed = records.count { it.status == HabitStatus.FAILED }
        val pending = records.count { it.status == HabitStatus.PENDING }
        val evaluated = completed + failed

        val completionRate = if (evaluated == 0) {
            0.0
        } else {
            completed.toDouble() / evaluated * 100.0
        }

        return Summary(
            total = total,
            completed = completed,
            failed = failed,
            pending = pending,
            evaluated = evaluated,
            completionRate = completionRate
        )
    }
}
