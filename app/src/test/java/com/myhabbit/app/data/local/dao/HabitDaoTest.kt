package com.myhabbit.app.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HabitDaoTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var habitDao: HabitDao
    private lateinit var habitRecordDao: HabitRecordDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            MyHabbitDatabase::class.java
        ).allowMainThreadQueries().build()
        habitDao = database.habitDao()
        habitRecordDao = database.habitRecordDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun habit(
        id: String = "habit-001",
        title: String = "Belajar Inggris",
        category: String = "DAILY",
        isActive: Boolean = true
    ) = HabitEntity(
        id = id,
        title = title,
        category = category,
        description = null,
        isActive = isActive,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )

    @Test
    fun insertHabit_andRetrieveById() = runTest {
        habitDao.insertHabit(habit())

        val result = habitDao.getHabitById("habit-001")

        assertNotNull(result)
        assertEquals("Belajar Inggris", result?.title)
        assertEquals("DAILY", result?.category)
    }

    @Test
    fun insertDuplicateId_throws() = runTest {
        habitDao.insertHabit(habit())

        try {
            habitDao.insertHabit(habit())
            assertTrue("Duplicate insert should fail", false)
        } catch (e: Exception) {
            assertTrue(true)
        }
    }

    @Test
    fun softDelete_removesFromActiveHabitsButKeepsRow() = runTest {
        habitDao.insertHabit(habit())
        val now = System.currentTimeMillis()
        habitDao.softDeleteHabit("habit-001", now, now)

        val active = habitDao.getActiveHabits()
        val byId = habitDao.getHabitById("habit-001")

        assertTrue(active.isEmpty())
        assertNotNull(byId)
        assertNotNull(byId?.deletedAt)
    }

    @Test
    fun insertRecord_uniquePeriodKey_enforced() = runTest {
        habitDao.insertHabit(habit())
        val record = HabitRecordEntity(
            id = "record-001",
            habitId = "habit-001",
            periodType = "DAILY",
            periodKey = "2026-11-13",
            status = "PENDING",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        habitRecordDao.insertRecord(record)

        val duplicate = record.copy(id = "record-002")
        try {
            habitRecordDao.insertRecord(duplicate)
            assertTrue("Duplicate periodKey should fail", false)
        } catch (e: Exception) {
            assertTrue(true)
        }
    }

    @Test
    fun getRecord_byHabitIdAndPeriodKey() = runTest {
        habitDao.insertHabit(habit())
        val record = HabitRecordEntity(
            id = "record-001",
            habitId = "habit-001",
            periodType = "DAILY",
            periodKey = "2026-11-13",
            status = "COMPLETED",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        habitRecordDao.insertRecord(record)

        val result = habitRecordDao.getRecord("habit-001", "2026-11-13")
        val missing = habitRecordDao.getRecord("habit-001", "2026-11-12")

        assertNotNull(result)
        assertEquals("COMPLETED", result?.status)
        assertNull(missing)
    }

    @Test
    fun updateRecord_persistsStatusChange() = runTest {
        habitDao.insertHabit(habit())
        val record = HabitRecordEntity(
            id = "record-001",
            habitId = "habit-001",
            periodType = "DAILY",
            periodKey = "2026-11-13",
            status = "PENDING",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        habitRecordDao.insertRecord(record)

        habitRecordDao.updateRecord(record.copy(status = "FAILED", updatedAt = 999L))

        val result = habitRecordDao.getRecord("habit-001", "2026-11-13")
        assertEquals("FAILED", result?.status)
        assertEquals(999L, result?.updatedAt)
    }

    @Test
    fun observeActiveHabits_excludesSoftDeleted() = runTest {
        habitDao.insertHabit(habit())
        habitDao.insertHabit(habit(id = "habit-002", title = "Olahraga"))

        val now = System.currentTimeMillis()
        habitDao.softDeleteHabit("habit-001", now, now)

        val active = habitDao.observeActiveHabits().first()

        assertEquals(1, active.size)
        assertEquals("habit-002", active.first().id)
    }

    @Test
    fun getRecordsByPeriod_returnsOnlyThatPeriod() = runTest {
        habitDao.insertHabit(habit())
        habitRecordDao.insertRecord(
            HabitRecordEntity(
                id = "record-001",
                habitId = "habit-001",
                periodType = "DAILY",
                periodKey = "2026-11-13",
                status = "COMPLETED",
                createdAt = 1L,
                updatedAt = 1L
            )
        )
        habitRecordDao.insertRecord(
            HabitRecordEntity(
                id = "record-002",
                habitId = "habit-001",
                periodType = "WEEKLY",
                periodKey = "2026-W46",
                status = "PENDING",
                createdAt = 1L,
                updatedAt = 1L
            )
        )

        val records = habitRecordDao.getRecordsByPeriod("2026-11-13")

        assertEquals(1, records.size)
        assertEquals("record-001", records.first().id)
    }
}
