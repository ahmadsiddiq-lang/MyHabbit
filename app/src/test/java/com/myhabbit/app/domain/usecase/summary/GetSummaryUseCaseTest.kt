package com.myhabbit.app.domain.usecase.summary

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitRecord
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GetSummaryUseCaseTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var useCase: GetSummaryUseCase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        useCase = GetSummaryUseCase(database.habitRecordDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun seedRecords() {
        val recordRepository = HabitRecordRepositoryImpl(database.habitRecordDao())
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit("h1", "T", HabitCategory.DAILY, null, true, 1L, 1L, null)
        )
        recordRepository.saveRecord(
            HabitRecord("r1", "h1", HabitCategory.DAILY, "2026-11-12", HabitStatus.COMPLETED, 1L, 1L)
        )
        recordRepository.saveRecord(
            HabitRecord("r2", "h1", HabitCategory.DAILY, "2026-11-13", HabitStatus.FAILED, 1L, 1L)
        )
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit("h2", "U", HabitCategory.WEEKLY, null, true, 1L, 1L, null)
        )
        recordRepository.saveRecord(
            HabitRecord("r3", "h2", HabitCategory.WEEKLY, "2026-W46", HabitStatus.COMPLETED, 1L, 1L)
        )
    }

    @Test
    fun summaryAll_categories() = runTest {
        seedRecords()

        val summary = useCase(null)

        assertEquals(3, summary.total)
        assertEquals(2, summary.completed)
        assertEquals(1, summary.failed)
        assertEquals(0, summary.pending)
        assertEquals(3, summary.evaluated)
        assertEquals(66.6, summary.completionRate, 0.1)
    }

    @Test
    fun summaryByCategory_dailyOnly() = runTest {
        seedRecords()

        val summary = useCase(HabitCategory.DAILY)

        assertEquals(2, summary.total)
        assertEquals(1, summary.completed)
        assertEquals(1, summary.failed)
        assertEquals(50.0, summary.completionRate, 0.001)
    }

    @Test
    fun summaryByCategory_weeklyOnly() = runTest {
        seedRecords()

        val summary = useCase(HabitCategory.WEEKLY)

        assertEquals(1, summary.total)
        assertEquals(1, summary.completed)
        assertEquals(100.0, summary.completionRate, 0.001)
    }

    @Test
    fun summaryByCategory_monthlyEmpty_returnsZero() = runTest {
        seedRecords()

        val summary = useCase(HabitCategory.MONTHLY)

        assertEquals(0, summary.total)
        assertEquals(0.0, summary.completionRate, 0.001)
    }
}
