package com.myhabbit.app.domain.service

import com.myhabbit.app.domain.model.HabitCategory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class PeriodCalculatorTest {

    private lateinit var calculator: PeriodCalculator

    @Before
    fun setup() {
        calculator = PeriodCalculatorImpl()
    }

    // ============ DAILY ============

    @Test
    fun daily_returnsDateAsString() {
        assertEquals(
            "2026-10-31",
            calculator.getCurrentPeriodKey(HabitCategory.DAILY, LocalDate.of(2026, 10, 31))
        )
    }

    @Test
    fun daily_startAndEndDate_areSameDay() {
        val date = LocalDate.of(2026, 11, 1)
        val key = calculator.getCurrentPeriodKey(HabitCategory.DAILY, date)

        assertEquals(date, calculator.getPeriodStartDate(HabitCategory.DAILY, key))
        assertEquals(date, calculator.getPeriodEndDate(HabitCategory.DAILY, key))
    }

    // ============ WEEKLY ============

    @Test
    fun weekly_saturdayAndSundayOfSameWeek_haveSameKey() {
        // 2026-10-31 adalah Sabtu, 2026-11-01 adalah Minggu (akhir minggu yang sama, ISO)
        val saturday = LocalDate.of(2026, 10, 31)
        val sunday = LocalDate.of(2026, 11, 1)

        val saturdayKey = calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, saturday)
        val sundayKey = calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, sunday)

        assertEquals(saturdayKey, sundayKey)
        assertEquals("2026-W44", saturdayKey)
    }

    @Test
    fun weekly_mondayStartsNewWeek() {
        // 2026-11-02 adalah Senin — awal minggu baru
        assertEquals(
            "2026-W45",
            calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, LocalDate.of(2026, 11, 2))
        )
    }

    @Test
    fun weekly_startDateIsMonday_endDateIsSunday() {
        val start = calculator.getPeriodStartDate(HabitCategory.WEEKLY, "2026-W44")
        val end = calculator.getPeriodEndDate(HabitCategory.WEEKLY, "2026-W44")

        assertEquals(LocalDate.of(2026, 10, 26), start) // Senin
        assertEquals(LocalDate.of(2026, 11, 1), end) // Minggu
        assertEquals(java.time.DayOfWeek.MONDAY, start.dayOfWeek)
        assertEquals(java.time.DayOfWeek.SUNDAY, end.dayOfWeek)
    }

    @Test
    fun weekly_yearBoundary_week1ContainsFirstThursday() {
        // 2026-01-01 jatuh di Kamis → minggu 1 tahun ISO 2026
        assertEquals(
            "2026-W01",
            calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, LocalDate.of(2026, 1, 1))
        )

        // 2027-01-01 jatuh di Jumat → masih ISO minggu terakhir 2026
        assertEquals(
            "2026-W53",
            calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, LocalDate.of(2027, 1, 1))
        )
    }

    @Test
    fun weekly_period53_startAndEndDates() {
        // 2026 memiliki 53 minggu ISO
        val start = calculator.getPeriodStartDate(HabitCategory.WEEKLY, "2026-W53")
        val end = calculator.getPeriodEndDate(HabitCategory.WEEKLY, "2026-W53")

        assertEquals(LocalDate.of(2026, 12, 28), start)
        assertEquals(LocalDate.of(2027, 1, 3), end)
    }

    @Test
    fun weekly_roundTrip_keyToDatesToKey() {
        val date = LocalDate.of(2026, 2, 18) // tengah bulan, dekat boundary minggu
        val key = calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, date)

        val start = calculator.getPeriodStartDate(HabitCategory.WEEKLY, key)
        val roundTripKey = calculator.getCurrentPeriodKey(HabitCategory.WEEKLY, start)

        assertEquals(key, roundTripKey)
    }

    // ============ MONTHLY ============

    @Test
    fun monthly_returnsYearMonth() {
        assertEquals(
            "2026-10",
            calculator.getCurrentPeriodKey(HabitCategory.MONTHLY, LocalDate.of(2026, 10, 31))
        )
    }

    @Test
    fun monthly_startDateIsFirst_endDateIsLastDay() {
        val start = calculator.getPeriodStartDate(HabitCategory.MONTHLY, "2026-10")
        val end = calculator.getPeriodEndDate(HabitCategory.MONTHLY, "2026-10")

        assertEquals(LocalDate.of(2026, 10, 1), start)
        assertEquals(LocalDate.of(2026, 10, 31), end)
    }

    @Test
    fun monthly_februaryLeapYear_endDateIs29() {
        // 2024 tahun kabisat
        val end = calculator.getPeriodEndDate(HabitCategory.MONTHLY, "2024-02")

        assertEquals(LocalDate.of(2024, 2, 29), end)
    }

    @Test
    fun monthly_februaryNonLeapYear_endDateIs28() {
        // 2026 bukan tahun kabisat
        val end = calculator.getPeriodEndDate(HabitCategory.MONTHLY, "2026-02")

        assertEquals(LocalDate.of(2026, 2, 28), end)
    }

    @Test
    fun monthly_yearBoundary() {
        assertEquals(
            "2026-12",
            calculator.getCurrentPeriodKey(HabitCategory.MONTHLY, LocalDate.of(2026, 12, 31))
        )
        assertEquals(
            "2027-01",
            calculator.getCurrentPeriodKey(HabitCategory.MONTHLY, LocalDate.of(2027, 1, 1))
        )
    }

    // ============ INVALID KEYS ============

    @Test(expected = IllegalArgumentException::class)
    fun weekly_invalidKeyFormat_throws() {
        calculator.getPeriodStartDate(HabitCategory.WEEKLY, "2026-W4")
    }

    @Test(expected = IllegalArgumentException::class)
    fun monthly_invalidKeyFormat_throws() {
        calculator.getPeriodStartDate(HabitCategory.MONTHLY, "2026-10-31")
    }

    // ============ NEW PERIOD = NEW PENDING RECORD (no old data deleted) ============

    @Test
    fun daily_periodKeysDifferAcrossDays() {
        val day1 = calculator.getCurrentPeriodKey(HabitCategory.DAILY, LocalDate.of(2026, 10, 31))
        val day2 = calculator.getCurrentPeriodKey(HabitCategory.DAILY, LocalDate.of(2026, 11, 1))

        // Key berbeda → record baru akan dibuat, record lama tidak diubah/dihapus
        org.junit.Assert.assertNotEquals(day1, day2)
    }
}
