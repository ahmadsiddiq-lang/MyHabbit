package com.myhabbit.app.domain.usecase.summary

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.service.PeriodCalculatorImpl
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GetTrendUseCaseTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var useCase: GetTrendUseCase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        useCase = GetTrendUseCase(
            database.habitRecordDao(),
            PeriodCalculatorImpl()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun seedDailyRecords() {
        val recordRepository = HabitRecordRepositoryImpl(database.habitRecordDao())
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit("h1", "T", HabitCategory.DAILY, null, true, 1L, 1L, null)
        )
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit("h2", "U", HabitCategory.DAILY, null, true, 1L, 1L, null)
        )
        // Kemarin (h1): selesai → 100%
        val yesterday = LocalDate.now().minusDays(1).toString()
        recordRepository.saveRecord(
            HabitRecord("r1", "h1", HabitCategory.DAILY, yesterday, HabitStatus.COMPLETED, 1L, 1L)
        )
        // Hari ini: h1 selesai + h2 gagal → 50%
        val today = LocalDate.now().toString()
        recordRepository.saveRecord(
            HabitRecord("r2", "h1", HabitCategory.DAILY, today, HabitStatus.COMPLETED, 1L, 1L)
        )
        recordRepository.saveRecord(
            HabitRecord("r3", "h2", HabitCategory.DAILY, today, HabitStatus.FAILED, 1L, 1L)
        )
    }

    @Test
    fun trendDaily_sevenPoints() = runTest {
        seedDailyRecords()

        val trend = useCase(HabitCategory.DAILY)

        assertEquals(7, trend.size)
    }

    @Test
    fun trendDaily_todayPercentageIsFifty() = runTest {
        seedDailyRecords()

        val trend = useCase(HabitCategory.DAILY)

        assertEquals(50.0, trend.last().percentage, 0.001)
    }

    @Test
    fun trendDaily_yesterdayPercentageIsHundred() = runTest {
        seedDailyRecords()

        val trend = useCase(HabitCategory.DAILY)

        assertEquals(100.0, trend[5].percentage, 0.001)
    }

    @Test
    fun trendDaily_emptyDaysCountAsZero() = runTest {
        seedDailyRecords()

        val trend = useCase(HabitCategory.DAILY)

        // 6 hari sebelum kemarin tanpa record → 0%
        assertEquals(0.0, trend.first().percentage, 0.001)
        assertEquals(0.0, trend[1].percentage, 0.001)
    }

    @Test
    fun trendDaily_labelsAreIndonesianDayAbbrev() = runTest {
        val trend = useCase(HabitCategory.DAILY)

        val today = LocalDate.now()
        val expectedLabel = when (today.dayOfWeek.name) {
            "MONDAY" -> "Sen"
            "TUESDAY" -> "Sel"
            "WEDNESDAY" -> "Rab"
            "THURSDAY" -> "Kam"
            "FRIDAY" -> "Jum"
            "SATURDAY" -> "Sab"
            else -> "Min"
        }
        assertEquals(expectedLabel, trend.last().label)
    }

    @Test
    fun trendWeekly_usesWeekNumbers() = runTest {
        val trend = useCase(HabitCategory.WEEKLY)

        assertEquals(7, trend.size)
        // Label minggu: angka ISO week
        assertTrue(trend.all { it.label.toIntOrNull() != null })
    }

    @Test
    fun trendMonthly_usesIndonesianMonthAbbrev() = runTest {
        val trend = useCase(HabitCategory.MONTHLY)

        assertEquals(7, trend.size)
        val validLabels = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
        assertTrue(trend.all { it.label in validLabels })
    }
}
