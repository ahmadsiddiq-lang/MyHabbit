package com.myhabbit.app.data.backup

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import com.myhabbit.app.data.local.entity.HabitEntity
import com.myhabbit.app.data.local.entity.HabitRecordEntity
import com.myhabbit.app.data.local.entity.SettingsEntity
import com.myhabbit.app.data.local.entity.UserEntity
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
class BackupExporterImporterTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var exporter: BackupExporter
    private lateinit var importer: BackupImporter
    private val serializer = BackupSerializer()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        exporter = BackupExporter(
            database.habitDao(),
            database.habitRecordDao(),
            database.userDao(),
            database.settingsDao()
        )
        importer = BackupImporter(database, serializer, BackupValidator())
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun seedDatabase() {
        database.habitDao().insertHabit(
            HabitEntity("h1", "Belajar", "DAILY", "15 menit", true, 1L, 2L, null)
        )
        database.habitDao().insertHabit(
            // soft-deleted habit harus ikut di-export (§16.3)
            HabitEntity("h2", "Lama", "WEEKLY", null, false, 1L, 3L, 5L)
        )
        database.habitRecordDao().insertRecord(
            HabitRecordEntity("r1", "h1", "DAILY", "2026-11-13", "COMPLETED", 1L, 2L)
        )
        database.userDao().insertUser(
            UserEntity("gid", "gid", "Ahmad", "ahmad@example.com", null, 1L, 1L)
        )
        database.settingsDao().insertSettings(
            SettingsEntity(1, "system", false, null)
        )
    }

    @Test
    fun export_includesAllData() = runTest {
        seedDatabase()

        val data = exporter.export()

        assertEquals(BACKUP_SCHEMA_VERSION, data.schemaVersion)
        assertEquals(2, data.habits.size)
        assertEquals(1, data.records.size)
        assertEquals("Ahmad", data.user?.name)
        assertNotNull(data.settings)
        assertTrue(data.app.name == "MyHabbit")
    }

    @Test
    fun exportImport_roundTrip_restoresData() = runTest {
        seedDatabase()
        val json = serializer.serialize(exporter.export())

        // Kosongkan database (simulasi device baru)
        database.habitRecordDao().deleteAllRecords()
        database.habitDao().deleteAllHabits()
        database.userDao().deleteAllUsers()
        assertEquals(0, database.habitDao().getAllHabits().size)

        val result = importer.import(json)

        assertEquals(BackupValidation.Valid, result)
        assertEquals(2, database.habitDao().getAllHabits().size)
        assertEquals(1, database.habitRecordDao().getAllRecords().size)
        assertEquals("Ahmad", database.userDao().getCurrentUser()?.name)
        assertEquals("system", database.settingsDao().getSettings()?.theme)
    }

    @Test
    fun import_invalidJson_doesNotCorruptData() = runTest {
        seedDatabase()

        val result = importer.import("bukan json {{{")

        assertTrue(result is BackupValidation.Invalid)
        // Data lama tetap utuh
        assertEquals(2, database.habitDao().getAllHabits().size)
    }

    @Test
    fun import_validationFailure_doesNotReplaceData() = runTest {
        seedDatabase()

        val badJson = serializer.serialize(
            BackupData(
                schemaVersion = 2, // versi masa depan → ditolak
                app = AppJson("MyHabbit", "1.0.0"),
                exportedAt = 1L
            )
        )

        val result = importer.import(badJson)

        assertTrue(result is BackupValidation.Invalid)
        assertEquals(2, database.habitDao().getAllHabits().size)
        assertEquals(1, database.habitRecordDao().getAllRecords().size)
    }

    @Test
    fun import_lowercaseEnums_normalizesToInternalUppercase() = runTest {
        val json = serializer.serialize(
            BackupData(
                schemaVersion = 1,
                app = AppJson("MyHabbit", "1.0.0"),
                exportedAt = 1L,
                habits = listOf(
                    HabitJson("h1", "Belajar", "daily", null, true, 1L, 1L, null)
                ),
                records = listOf(
                    RecordJson("r1", "h1", "daily", "2026-11-13", "completed", 1L, 1L)
                )
            )
        )

        val result = importer.import(json)

        assertEquals(BackupValidation.Valid, result)
        assertEquals("DAILY", database.habitDao().getAllHabits()[0].category)
        assertEquals("COMPLETED", database.habitRecordDao().getAllRecords()[0].status)
    }

    @Test
    fun export_emptyDatabase_producesEmptySections() = runTest {
        val data = exporter.export()

        assertTrue(data.habits.isEmpty())
        assertTrue(data.records.isEmpty())
        assertNull(data.user)
    }

    @Test
    fun import_settingsWithoutLastBackupAt_keepsLocalNewerTimestamp() = runTest {
        // Simulasi retest end-to-end: device sudah backup (lastBackupAt terisi),
        // lalu restore file backup yang dibuat sebelum backup pertama (null).
        val json = serializer.serialize(
            BackupData(
                schemaVersion = 1,
                app = AppJson("MyHabbit", "1.0.0"),
                exportedAt = 1L,
                settings = SettingsJson(theme = "dark", notificationsEnabled = true, lastBackupAt = null)
            )
        )
        database.settingsDao().insertSettings(
            SettingsEntity(1, "system", false, 1731800000000L)
        )

        val result = importer.import(json)

        assertEquals(BackupValidation.Valid, result)
        val settings = database.settingsDao().getSettings()
        assertEquals("dark", settings?.theme)
        assertEquals(true, settings?.notificationsEnabled)
        // Timestamp lokal TIDAK hilang
        assertEquals(1731800000000L, settings?.lastBackupAt)
    }

    @Test
    fun import_settingsWithNewerLastBackupAt_usesBackupTimestamp() = runTest {
        // Backup lebih baru dari device → pakai timestamp backup
        val json = serializer.serialize(
            BackupData(
                schemaVersion = 1,
                app = AppJson("MyHabbit", "1.0.0"),
                exportedAt = 1L,
                settings = SettingsJson(
                    theme = "dark",
                    notificationsEnabled = true,
                    lastBackupAt = 9900000000000L
                )
            )
        )
        database.settingsDao().insertSettings(
            SettingsEntity(1, "system", false, 1731800000000L)
        )

        val result = importer.import(json)

        assertEquals(BackupValidation.Valid, result)
        assertEquals(9900000000000L, database.settingsDao().getSettings()?.lastBackupAt)
    }
}
