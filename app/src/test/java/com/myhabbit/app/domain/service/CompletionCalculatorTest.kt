package com.myhabbit.app.domain.service

import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class CompletionCalculatorTest {

    private fun record(
        id: String,
        periodType: HabitCategory = HabitCategory.DAILY,
        periodKey: String = "2026-11-13",
        status: HabitStatus
    ) = HabitRecord(
        id = id,
        habitId = "habit_$id",
        periodType = periodType,
        periodKey = periodKey,
        status = status,
        createdAt = 1L,
        updatedAt = 1L
    )

    @Test
    fun calculate_basicFormula() {
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.COMPLETED),
                record("r2", status = HabitStatus.COMPLETED),
                record("r3", status = HabitStatus.COMPLETED),
                record("r4", status = HabitStatus.COMPLETED),
                record("r5", status = HabitStatus.COMPLETED),
                record("r6", status = HabitStatus.COMPLETED),
                record("r7", status = HabitStatus.COMPLETED),
                record("r8", status = HabitStatus.COMPLETED),
                record("r9", status = HabitStatus.FAILED),
                record("r10", status = HabitStatus.FAILED),
                record("r11", status = HabitStatus.PENDING),
                record("r12", status = HabitStatus.PENDING),
                record("r13", status = HabitStatus.PENDING),
                record("r14", status = HabitStatus.PENDING),
                record("r15", status = HabitStatus.PENDING)
            )
        )

        // PRD.md §24: 8 completed, 2 failed, 5 pending → 8/10*100 = 80%
        assertEquals(15, summary.total)
        assertEquals(8, summary.completed)
        assertEquals(2, summary.failed)
        assertEquals(5, summary.pending)
        assertEquals(10, summary.evaluated)
        assertEquals(80.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_pendingNotInDenominator() {
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.COMPLETED),
                record("r2", status = HabitStatus.FAILED),
                record("r3", status = HabitStatus.PENDING),
                record("r4", status = HabitStatus.PENDING),
                record("r5", status = HabitStatus.PENDING)
            )
        )

        assertEquals(50.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_evaluatedZero_returnsZeroRate() {
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.PENDING),
                record("r2", status = HabitStatus.PENDING)
            )
        )

        assertEquals(2, summary.total)
        assertEquals(0, summary.evaluated)
        assertEquals(0.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_emptyRecords_returnsZeroSummary() {
        val summary = CompletionCalculator.calculate(emptyList())

        assertEquals(0, summary.total)
        assertEquals(0, summary.completed)
        assertEquals(0, summary.failed)
        assertEquals(0, summary.pending)
        assertEquals(0, summary.evaluated)
        assertEquals(0.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_allCompleted_returns100() {
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.COMPLETED),
                record("r2", status = HabitStatus.COMPLETED)
            )
        )

        assertEquals(100.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_allFailed_returns0() {
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.FAILED),
                record("r2", status = HabitStatus.FAILED)
            )
        )

        assertEquals(0.0, summary.completionRate, 0.001)
    }

    @Test
    fun calculate_rataRataTidakRataRatakanPersen() {
        // ARCHITECTURE.md §12.3: gunakan agregat, bukan rata-rata persen
        // Periode 1: 1 completed (100%), Periode 2: 1 failed (0%) → agregat 50%
        val summary = CompletionCalculator.calculate(
            listOf(
                record("r1", status = HabitStatus.COMPLETED),
                record("r2", status = HabitStatus.FAILED)
            )
        )

        assertEquals(50.0, summary.completionRate, 0.001)
    }
}
