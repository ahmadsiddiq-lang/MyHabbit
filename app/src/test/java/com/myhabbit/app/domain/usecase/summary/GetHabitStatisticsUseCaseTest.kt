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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GetHabitStatisticsUseCaseTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var useCase: GetHabitStatisticsUseCase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        useCase = GetHabitStatisticsUseCase(database.habitDao(), database.habitRecordDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun seedData() {
        val habitRepository = HabitRepositoryImpl(database.habitDao())
        val recordRepository = HabitRecordRepositoryImpl(database.habitRecordDao())

        // h1: 3 completed, 1 failed → 75%
        habitRepository.insertHabit(Habit("h1", "Lari", HabitCategory.DAILY, null, true, 1L, 1L, null))
        // h2: 1 completed, 1 failed → 50%
        habitRepository.insertHabit(Habit("h2", "Baca", HabitCategory.WEEKLY, null, true, 1L, 1L, null))
        // h3: hanya pending → di-exclude
        habitRepository.insertHabit(Habit("h3", "Pending saja", HabitCategory.DAILY, null, true, 1L, 1L, null))

        suspend fun record(id: String, habitId: String, type: HabitCategory, key: String, status: HabitStatus) {
            recordRepository.saveRecord(HabitRecord(id, habitId, type, key, status, 1L, 1L))
        }

        record("r1", "h1", HabitCategory.DAILY, "2026-11-10", HabitStatus.COMPLETED)
        record("r2", "h1", HabitCategory.DAILY, "2026-11-11", HabitStatus.COMPLETED)
        record("r3", "h1", HabitCategory.DAILY, "2026-11-12", HabitStatus.COMPLETED)
        record("r4", "h1", HabitCategory.DAILY, "2026-11-13", HabitStatus.FAILED)

        record("r5", "h2", HabitCategory.WEEKLY, "2026-W45", HabitStatus.COMPLETED)
        record("r6", "h2", HabitCategory.WEEKLY, "2026-W46", HabitStatus.FAILED)

        record("r7", "h3", HabitCategory.DAILY, "2026-11-13", HabitStatus.PENDING)
    }

    @Test
    fun statistics_perHabitRateSortedDescending() = runTest {
        seedData()

        val stats = useCase(null)

        // h3 (hanya pending) di-exclude
        assertEquals(2, stats.size)
        assertEquals("h1", stats[0].habitId)
        assertEquals(75.0, stats[0].completionRate, 0.001)
        assertEquals("h2", stats[1].habitId)
        assertEquals(50.0, stats[1].completionRate, 0.001)
    }

    @Test
    fun statistics_countsCompletedAndFailed() = runTest {
        seedData()

        val stats = useCase(null)
        val h1 = stats.first { it.habitId == "h1" }

        assertEquals(3, h1.completed)
        assertEquals(1, h1.failed)
        assertEquals("Lari", h1.habitTitle)
    }

    @Test
    fun statistics_filterByCategory() = runTest {
        seedData()

        val weeklyStats = useCase(HabitCategory.WEEKLY)

        assertEquals(1, weeklyStats.size)
        assertEquals("h2", weeklyStats[0].habitId)
        assertEquals(50.0, weeklyStats[0].completionRate, 0.001)
    }

    @Test
    fun statistics_noRecords_returnsEmpty() = runTest {
        val stats = useCase(null)

        assertTrue(stats.isEmpty())
    }

    @Test
    fun statistics_excludeHabitWithOnlyPending() = runTest {
        seedData()

        val stats = useCase(null)

        assertTrue(stats.none { it.habitId == "h3" })
    }
}
