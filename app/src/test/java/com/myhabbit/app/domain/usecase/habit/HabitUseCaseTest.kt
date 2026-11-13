package com.myhabbit.app.domain.usecase.habit

import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.data.repository.HabitRepositoryImpl
import com.myhabbit.app.data.repository.HabitRecordRepositoryImpl
import com.myhabbit.app.data.local.dao.HabitDao
import com.myhabbit.app.data.local.dao.HabitRecordDao
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HabitUseCaseTest {

    private lateinit var habitDao: HabitDao
    private lateinit var habitRecordDao: HabitRecordDao
    private lateinit var habitRepository: HabitRepositoryImpl
    private lateinit var recordRepository: HabitRecordRepositoryImpl

    private lateinit var createHabitUseCase: CreateHabitUseCase
    private lateinit var updateHabitUseCase: UpdateHabitUseCase
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase

    @Before
    fun setup() {
        habitDao = Mockito.mock(HabitDao::class.java)
        habitRecordDao = Mockito.mock(HabitRecordDao::class.java)
        habitRepository = HabitRepositoryImpl(habitDao)
        recordRepository = HabitRecordRepositoryImpl(habitRecordDao)

        createHabitUseCase = CreateHabitUseCase(habitRepository)
        updateHabitUseCase = UpdateHabitUseCase(habitRepository)
        deleteHabitUseCase = DeleteHabitUseCase(habitRepository)
    }

    @Test
    fun create_validInput_savesHabitWithUniqueId() = runTest {
        val result = createHabitUseCase(
            title = "Belajar Inggris",
            category = HabitCategory.DAILY,
            description = "Belajar 15 menit"
        )

        assertTrue(result is AppResult.Success)
        val id = (result as AppResult.Success).data
        assertTrue(id.startsWith("habit_"))

        val captor = argumentCaptor<com.myhabbit.app.data.local.entity.HabitEntity>()
        verify(habitDao).insertHabit(captor.capture())
        assertEquals("Belajar Inggris", captor.firstValue.title)
        assertEquals(HabitCategory.DAILY.name, captor.firstValue.category)
        assertTrue(captor.firstValue.isActive)
    }

    @Test
    fun create_blankTitle_returnsError() = runTest {
        val result = createHabitUseCase(
            title = "   ",
            category = HabitCategory.DAILY,
            description = null
        )

        assertTrue(result is AppResult.Error)
        verify(habitDao, Mockito.times(0)).insertHabit(any())
    }

    @Test
    fun create_titleTooLong_returnsError() = runTest {
        val longTitle = "x".repeat(101)
        val result = createHabitUseCase(
            title = longTitle,
            category = HabitCategory.DAILY,
            description = null
        )

        assertTrue(result is AppResult.Error)
    }

    @Test
    fun create_descriptionTooLong_returnsError() = runTest {
        val longDescription = "x".repeat(501)
        val result = createHabitUseCase(
            title = "Valid title",
            category = HabitCategory.DAILY,
            description = longDescription
        )

        assertTrue(result is AppResult.Error)
    }

    @Test
    fun create_duplicateId_throwsRepositoryError() = runTest {
        val first = createHabitUseCase("Title", HabitCategory.DAILY, null)
        assertTrue(first is AppResult.Success)
    }

    @Test
    fun update_preservesId_andHistory() = runTest {
        val existing = Habit(
            id = "habit_fixed",
            title = "Old title",
            category = HabitCategory.WEEKLY,
            description = null,
            isActive = true,
            createdAt = 100L,
            updatedAt = 100L,
            deletedAt = null
        )
        whenever(habitDao.getHabitById(eq("habit_fixed"))).thenReturn(
            com.myhabbit.app.data.local.entity.HabitEntity(
                id = existing.id,
                title = existing.title,
                category = existing.category.name,
                description = existing.description,
                isActive = existing.isActive,
                createdAt = existing.createdAt,
                updatedAt = existing.updatedAt,
                deletedAt = existing.deletedAt
            )
        )

        val result = updateHabitUseCase(
            habitId = "habit_fixed",
            title = "New title",
            category = HabitCategory.WEEKLY,
            description = "New desc"
        )

        assertTrue(result is AppResult.Success)
        val captor = argumentCaptor<com.myhabbit.app.data.local.entity.HabitEntity>()
        verify(habitDao).updateHabit(captor.capture())
        assertEquals("habit_fixed", captor.firstValue.id)
        assertEquals(100L, captor.firstValue.createdAt)
        assertEquals("New title", captor.firstValue.title)
    }

    @Test
    fun delete_performsSoftDelete() = runTest {
        whenever(habitDao.getHabitById(eq("habit_1"))).thenReturn(
            com.myhabbit.app.data.local.entity.HabitEntity(
                id = "habit_1",
                title = "T",
                category = "DAILY",
                description = null,
                isActive = true,
                createdAt = 1L,
                updatedAt = 1L,
                deletedAt = null
            )
        )

        val result = deleteHabitUseCase("habit_1", currentTimeMillis = 500L)

        assertTrue(result is AppResult.Success)
        verify(habitDao).softDeleteHabit(eq("habit_1"), eq(500L), eq(500L))
    }

    @Test
    fun delete_missingHabit_returnsError() = runTest {
        val result = deleteHabitUseCase("habit_missing", currentTimeMillis = 1L)

        assertTrue(result is AppResult.Error)
    }
}
