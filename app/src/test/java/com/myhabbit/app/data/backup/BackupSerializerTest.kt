package com.myhabbit.app.data.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupSerializerTest {

    private val serializer = BackupSerializer()

    private val sample = BackupData(
        schemaVersion = 1,
        app = AppJson(name = "MyHabbit", version = "1.0.0"),
        exportedAt = 1763020800000L,
        user = UserJson(id = "gid", name = "Ahmad", email = "ahmad@example.com", photoUrl = null),
        habits = listOf(
            HabitJson(
                id = "h1",
                title = "Belajar",
                category = "daily",
                description = "15 menit",
                isActive = true,
                createdAt = 1L,
                updatedAt = 2L,
                deletedAt = null
            )
        ),
        records = listOf(
            RecordJson(
                id = "r1",
                habitId = "h1",
                periodType = "daily",
                periodKey = "2026-11-13",
                status = "completed",
                createdAt = 1L,
                updatedAt = 2L
            )
        ),
        settings = SettingsJson(theme = "system", notificationsEnabled = false, lastBackupAt = null)
    )

    @Test
    fun serialize_containsSchemaVersionAndSections() {
        val json = serializer.serialize(sample)

        assertTrue(json.contains("\"schemaVersion\": 1"))
        assertTrue(json.contains("\"MyHabbit\""))
        assertTrue(json.contains("\"habits\""))
        assertTrue(json.contains("\"records\""))
    }

    @Test
    fun roundTrip_preservesData() {
        val json = serializer.serialize(sample)
        val parsed = serializer.deserialize(json)

        assertEquals(sample, parsed)
    }

    @Test
    fun deserialize_malformedJson_returnsNull() {
        val parsed = serializer.deserialize("bukan json {{{")

        assertTrue(parsed == null)
    }

    @Test
    fun deserialize_emptyHabitsRecords() {
        val minimal = BackupData(
            schemaVersion = 1,
            app = AppJson(name = "MyHabbit", version = "1.0.0"),
            exportedAt = 1L
        )
        val json = serializer.serialize(minimal)
        val parsed = serializer.deserialize(json)

        assertEquals(minimal, parsed)
        assertTrue(parsed?.habits?.isEmpty() == true)
    }

    @Test
    fun serialize_deserialize_userNullable() {
        val noUser = sample.copy(user = null)
        val json = serializer.serialize(noUser)

        val parsed = serializer.deserialize(json)

        assertEquals(noUser, parsed)
        assertTrue(json.contains("\"user\": null"))
    }
}
