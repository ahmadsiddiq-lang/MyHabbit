package com.myhabbit.app.domain.usecase.summary

import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.data.mapper.toDomain
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.service.PeriodCalculator
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

/**
 * Tren completion 7 periode terakhir (7 hari / 7 minggu / 7 bulan)
 * sesuai kategori (mockup UI.png #7 "Tren 7 Hari Terakhir").
 * ViewModel menyiapkan data; komponen chart hanya menggambar.
 */
class GetTrendUseCase @Inject constructor(
    private val habitRecordDao: HabitRecordDao,
    private val periodCalculator: PeriodCalculator
) {

    suspend operator fun invoke(
        category: HabitCategory
    ): List<TrendPoint> {
        val records = habitRecordDao.getAllRecords()
            .map { it.toDomain() }
            .filter { it.periodType == category }
            .groupBy { it.periodKey }

        val today = LocalDate.now()
        return (6 downTo 0).map { offset ->
            val date = when (category) {
                HabitCategory.DAILY -> today.minusDays(offset.toLong())
                HabitCategory.WEEKLY -> today.minusWeeks(offset.toLong())
                HabitCategory.MONTHLY -> today.minusMonths(offset.toLong())
            }
            val key = periodCalculator.getCurrentPeriodKey(category, date)
            val periodRecords = records[key].orEmpty()
            val completed = periodRecords.count { it.status == HabitStatus.COMPLETED }
            val evaluated = periodRecords.count { it.status != HabitStatus.PENDING }
            val rate = if (evaluated == 0) 0.0 else completed * 100.0 / evaluated
            TrendPoint(label = periodLabel(category, date), percentage = rate)
        }
    }

    private fun periodLabel(category: HabitCategory, date: LocalDate): String = when (category) {
        HabitCategory.DAILY -> when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "Sen"
            DayOfWeek.TUESDAY -> "Sel"
            DayOfWeek.WEDNESDAY -> "Rab"
            DayOfWeek.THURSDAY -> "Kam"
            DayOfWeek.FRIDAY -> "Jum"
            DayOfWeek.SATURDAY -> "Sab"
            DayOfWeek.SUNDAY -> "Min"
        }
        HabitCategory.WEEKLY -> {
            val key = periodCalculator.getCurrentPeriodKey(category, date)
            key.substringAfter("-W")
        }
        HabitCategory.MONTHLY -> when (date.month) {
            Month.JANUARY -> "Jan"
            Month.FEBRUARY -> "Feb"
            Month.MARCH -> "Mar"
            Month.APRIL -> "Apr"
            Month.MAY -> "Mei"
            Month.JUNE -> "Jun"
            Month.JULY -> "Jul"
            Month.AUGUST -> "Agu"
            Month.SEPTEMBER -> "Sep"
            Month.OCTOBER -> "Okt"
            Month.NOVEMBER -> "Nov"
            Month.DECEMBER -> "Des"
        }
    }
}
