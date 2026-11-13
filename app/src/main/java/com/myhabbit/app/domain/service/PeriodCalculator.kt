package com.myhabbit.app.domain.service

import com.myhabbit.app.domain.model.HabitCategory
import java.time.LocalDate

/**
 * Perhitungan periode untuk setiap kategori activity.
 *
 * Format period key (ARCHITECTURE.md §8):
 * - Daily:   YYYY-MM-DD   contoh: 2026-10-31
 * - Weekly:  YYYY-Www     contoh: 2026-W44 (minggu Senin s/d Minggu, ISO)
 * - Monthly: YYYY-MM      contoh: 2026-10
 *
 * Semua perhitungan menggunakan tanggal lokal device (PRD.md §52).
 */
interface PeriodCalculator {

    fun getCurrentPeriodKey(
        category: HabitCategory,
        date: LocalDate
    ): String

    fun getPeriodStartDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate

    fun getPeriodEndDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate
}
