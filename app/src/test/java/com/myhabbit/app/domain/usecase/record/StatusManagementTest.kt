package com.myhabbit.app.domain.usecase.record

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.model.HabitStatus
import com.myhabbit.app.domain.service.PeriodCalculatorImpl
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class StatusManagementTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var recordRepository: HabitRecordRepositoryImpl
    private lateinit var updateStatus: UpdateHabitStatusUseCase
    private lateinit var getStatus: GetHabitStatusUseCase

    private val today = LocalDate.of(2026, 11, 13) // Jumat

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recordRepository = HabitRecordRepositoryImpl(database.habitRecordDao())
        val calculator = PeriodCalculatorImpl()
        updateStatus = UpdateHabitStatusUseCase(recordRepository, calculator)
        getStatus = GetHabitStatusUseCase(recordRepository, calculator)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun seedHabit(
        id: String = "habit-001",
        category: HabitCategory = HabitCategory.DAILY
    ) {
        HabitRepositoryImpl(database.habitDao()).insertHabit(
            Habit(
                id = id,
                title = "Test habit",
                category = category,
                description = null,
                isActive = true,
                createdAt = 1L,
                updatedAt = 1L,
                deletedAt = null
            )
        )
    }

    @Test
    fun newHabitWithoutRecord_showsPending() = runTest {
        seedHabit()

        val status = getStatus("habit-001", HabitCategory.DAILY, today)

        assertEquals(HabitStatus.PENDING, status)
    }

    @Test
    fun markCompleted_createsRecord() = runTest {
        seedHabit()

        val result = updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.COMPLETED, today)

        assertTrue(result is AppResult.Success)
        val record = recordRepository.getRecord("habit-001", "2026-11-13")
        assertNotNull(record)
        assertEquals(HabitStatus.COMPLETED, record?.status)
    }

    @Test
    fun changeStatus_updatesSameRecord_notDuplicate() = runTest {
        seedHabit()
        updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.COMPLETED, today)

        val result = updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.FAILED, today)

        assertTrue(result is AppResult.Success)
        val allRecords = recordRepository.getRecordsByHabit("habit-001")
        assertEquals(1, allRecords.size)
        assertEquals(HabitStatus.FAILED, allRecords.first().status)
    }

    @Test
    fun backToPending_updatesRecordStatus() = runTest {
        seedHabit()
        updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.FAILED, today)

        val result = updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.PENDING, today)

        assertTrue(result is AppResult.Success)
        val status = getStatus("habit-001", HabitCategory.DAILY, today)
        assertEquals(HabitStatus.PENDING, status)
        // Record tetap ada (status pending disimpan, bukan dihapus)
        assertEquals(1, recordRepository.getRecordsByHabit("habit-001").size)
    }

    @Test
    fun newPeriod_hasFreshPendingStatus() = runTest {
        seedHabit()
        updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.COMPLETED, today)

        // Hari berikutnya
        val nextDay = today.plusDays(1)
        val status = getStatus("habit-001", HabitCategory.DAILY, nextDay)

        assertEquals(HabitStatus.PENDING, status)
        // Record hari sebelumnya tetap ada
        assertEquals(HabitStatus.COMPLETED, getStatus("habit-001", HabitCategory.DAILY, today))
        assertEquals(1, recordRepository.getRecordsByHabit("habit-001").size)
    }

    @Test
    fun weeklyHabit_usesWeeklyPeriodKey() = runTest {
        seedHabit(category = HabitCategory.WEEKLY)

        val result = updateStatus("habit-001", HabitCategory.WEEKLY, HabitStatus.COMPLETED, today)

        assertTrue(result is AppResult.Success)
        val record = recordRepository.getRecord("habit-001", "2026-W46")
        assertEquals(HabitStatus.COMPLETED, record?.status)
        assertEquals(HabitCategory.WEEKLY, record?.periodType)
    }

    @Test
    fun monthlyHabit_usesMonthlyPeriodKey() = runTest {
        seedHabit(category = HabitCategory.MONTHLY)

        val result = updateStatus("habit-001", HabitCategory.MONTHLY, HabitStatus.FAILED, today)

        assertTrue(result is AppResult.Success)
        val record = recordRepository.getRecord("habit-001", "2026-11")
        assertEquals(HabitStatus.FAILED, record?.status)
    }

    @Test
    fun recordPersistsAcrossRepositoryReads() = runTest {
        seedHabit()
        updateStatus("habit-001", HabitCategory.DAILY, HabitStatus.COMPLETED, today)

        val viaPeriod = recordRepository.getRecordsByPeriod("2026-11-13")

        assertEquals(1, viaPeriod.size)
        assertEquals(HabitStatus.COMPLETED, viaPeriod.first().status)
    }
}
