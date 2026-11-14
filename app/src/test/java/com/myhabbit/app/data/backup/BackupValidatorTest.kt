package com.myhabbit.app.data.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BackupValidatorTest {

    private lateinit var validator: BackupValidator

    @Before
    fun setup() {
        validator = BackupValidator()
    }

    private fun validData(): BackupData = BackupData(
        schemaVersion = 1,
        app = AppJson("MyHabbit", "1.0.0"),
        exportedAt = 1L,
        habits = listOf(
            HabitJson("h1", "Belajar", "daily", null, true, 1L, 1L, null),
            HabitJson("h2", "Lari", "weekly", null, true, 1L, 1L, null)
        ),
        records = listOf(
            RecordJson("r1", "h1", "daily", "2026-11-13", "completed", 1L, 1L)
        )
    )

    @Test
    fun validData_passes() {
        val result = validator.validate(validData())

        assertEquals(BackupValidation.Valid, result)
    }

    @Test
    fun futureSchemaVersion_rejected() {
        val result = validator.validate(validData().copy(schemaVersion = 2))

        assertTrue(result is BackupValidation.Invalid)
        assertTrue((result as BackupValidation.Invalid).reason.contains("Versi backup"))
    }

    @Test
    fun zeroSchemaVersion_rejected() {
        val result = validator.validate(validData().copy(schemaVersion = 0))

        assertTrue(result is BackupValidation.Invalid)
    }

    @Test
    fun habitWithBlankId_rejected() {
        val data = validData().copy(
            habits = listOf(HabitJson("", "Belajar", "daily", null, true, 1L, 1L, null))
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
    }

    @Test
    fun duplicateHabitIds_rejected() {
        val data = validData().copy(
            habits = listOf(
                HabitJson("h1", "A", "daily", null, true, 1L, 1L, null),
                HabitJson("h1", "B", "daily", null, true, 1L, 1L, null)
            )
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
        assertTrue((result as BackupValidation.Invalid).reason.contains("Habit id duplikat"))
    }

    @Test
    fun duplicateRecordIds_rejected() {
        val data = validData().copy(
            records = listOf(
                RecordJson("r1", "h1", "daily", "2026-11-13", "completed", 1L, 1L),
                RecordJson("r1", "h1", "daily", "2026-11-14", "failed", 1L, 1L)
            )
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
        assertTrue((result as BackupValidation.Invalid).reason.contains("Record id duplikat"))
    }

    @Test
    fun recordReferencingMissingHabit_rejected() {
        val data = validData().copy(
            records = listOf(
                RecordJson("r1", "habits-tidak-ada", "daily", "2026-11-13", "completed", 1L, 1L)
            )
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
        assertTrue((result as BackupValidation.Invalid).reason.contains("tidak ada di backup"))
    }

    @Test
    fun invalidStatusEnum_rejected() {
        val data = validData().copy(
            records = listOf(
                RecordJson("r1", "h1", "daily", "2026-11-13", "DONE", 1L, 1L)
            )
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
        assertTrue((result as BackupValidation.Invalid).reason.contains("status tidak valid"))
    }

    @Test
    fun invalidCategoryEnum_rejected() {
        val data = validData().copy(
            habits = listOf(HabitJson("h1", "Belajar", "yearly", null, true, 1L, 1L, null))
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
    }

    @Test
    fun blankTitle_rejected() {
        val data = validData().copy(
            habits = listOf(HabitJson("h1", "", "daily", null, true, 1L, 1L, null))
        )

        val result = validator.validate(data)

        assertTrue(result is BackupValidation.Invalid)
    }

    @Test
    fun lowercaseEnums_accepted() {
        // PRD §38 memakai lowercase — validator harus case-insensitive
        val result = validator.validate(validData())

        assertEquals(BackupValidation.Valid, result)
    }
}
