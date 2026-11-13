package com.myhabbit.app.domain.service

import com.myhabbit.app.domain.model.HabitCategory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/**
 * Implementasi PeriodCalculator menggunakan java.time.
 *
 * Weekly menggunakan ISO week (minggu dimulai Senin, berakhir Minggu)
 * dan WeekBasedYear agar boundary tahun dihitung dengan benar
 * (contoh: 1 Jan yang jatuh di minggu akhir tahun sebelumnya).
 */
class PeriodCalculatorImpl @Inject constructor() : PeriodCalculator {

    override fun getCurrentPeriodKey(
        category: HabitCategory,
        date: LocalDate
    ): String = when (category) {
        HabitCategory.DAILY -> date.toString() // YYYY-MM-DD
        HabitCategory.WEEKLY -> weeklyPeriodKey(date)
        HabitCategory.MONTHLY -> monthlyPeriodKey(date)
    }

    override fun getPeriodStartDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate = when (category) {
        HabitCategory.DAILY -> LocalDate.parse(periodKey)
        HabitCategory.WEEKLY -> parseWeekly(periodKey)
        HabitCategory.MONTHLY -> parseMonthly(periodKey)
    }

    override fun getPeriodEndDate(
        category: HabitCategory,
        periodKey: String
    ): LocalDate = when (category) {
        HabitCategory.DAILY -> LocalDate.parse(periodKey)
        HabitCategory.WEEKLY -> parseWeekly(periodKey).plusDays(6)
        HabitCategory.MONTHLY -> {
            val start = parseMonthly(periodKey)
            start.with(TemporalAdjusters.lastDayOfMonth())
        }
    }

    private fun weeklyPeriodKey(date: LocalDate): String {
        val weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
        val weekBasedYear = date.get(IsoFields.WEEK_BASED_YEAR)
        return "%d-W%02d".format(weekBasedYear, weekNumber)
    }

    private fun parseWeekly(periodKey: String): LocalDate {
        require(periodKey.matches(Regex("\\d{4}-W\\d{2}"))) {
            "Format weekly periodKey tidak valid: $periodKey"
        }
        val year = periodKey.substring(0, 4).toInt()
        val week = periodKey.substring(6, 8).toInt()
        // ISO: minggu ke-1 adalah minggu yang memuat Kamis pertama di Januari.
        // Tanggal awal minggu 1: Senin minggu yang memuat 4 Januari.
        val jan4 = LocalDate.of(year, Month.JANUARY, 4)
        val firstMondayOfIsoYear = jan4.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return firstMondayOfIsoYear.plusWeeks((week - 1).toLong())
    }

    private fun monthlyPeriodKey(date: LocalDate): String =
        "%d-%02d".format(date.year, date.monthValue)

    private fun parseMonthly(periodKey: String): LocalDate {
        require(periodKey.matches(Regex("\\d{4}-\\d{2}"))) {
            "Format monthly periodKey tidak valid: $periodKey"
        }
        val year = periodKey.substring(0, 4).toInt()
        val month = periodKey.substring(5, 7).toInt()
        return LocalDate.of(year, month, 1)
    }
}
