package com.myhabbit.app.data.repository

import android.content.ContentResolver
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import com.myhabbit.app.data.backup.BackupData
import com.myhabbit.app.data.backup.AppJson
import com.myhabbit.app.data.backup.BackupExporter
import com.myhabbit.app.data.backup.BackupImporter
import com.myhabbit.app.data.backup.BackupSerializer
import com.myhabbit.app.data.backup.BackupValidation
import com.myhabbit.app.data.backup.BackupValidator
import com.myhabbit.app.data.drive.GoogleDriveManager
import com.myhabbit.app.data.local.dao.SettingsDao
import com.myhabbit.app.data.local.entity.SettingsEntity
import java.io.ByteArrayInputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class BackupRepositoryImplTest {

    private lateinit var exporter: BackupExporter
    private lateinit var serializer: BackupSerializer
    private lateinit var importer: BackupImporter
    private lateinit var validator: BackupValidator
    private lateinit var driveManager: GoogleDriveManager
    private lateinit var settingsDao: SettingsDao
    private lateinit var repository: BackupRepositoryImpl

    private val activityContext: Context = Mockito.mock(Context::class.java)
    private val settingsFlow = MutableStateFlow<SettingsEntity?>(null)

    private val sampleData = BackupData(
        schemaVersion = 1,
        app = AppJson("MyHabbit", "1.0.0"),
        exportedAt = 1L
    )

    @Before
    fun setup() {
        exporter = Mockito.mock(BackupExporter::class.java)
        serializer = BackupSerializer()
        importer = Mockito.mock(BackupImporter::class.java)
        validator = BackupValidator()
        driveManager = Mockito.mock(GoogleDriveManager::class.java)
        settingsDao = Mockito.mock(SettingsDao::class.java)
        repository = BackupRepositoryImpl(
            context = Mockito.mock(Context::class.java),
            exporter = exporter,
            serializer = serializer,
            importer = importer,
            validator = validator,
            driveManager = driveManager,
            settingsDao = settingsDao
        )
    }

    @Test
    fun backupToDrive_success_updatesLastBackupAt() = runTest {
        whenever(exporter.export()).thenReturn(sampleData)
        whenever(driveManager.uploadBackup(any(), any())).thenReturn(Result.success(Unit))
        whenever(settingsDao.getSettings()).thenReturn(
            SettingsEntity(1, "system", false, null)
        )

        val error = repository.backupToDrive(activityContext)

        assertNull(error)
        Mockito.verify(settingsDao).updateLastBackupAt(any())
    }

    @Test
    fun backupToDrive_success_withoutSettingsRow_insertsDefaults() = runTest {
        whenever(exporter.export()).thenReturn(sampleData)
        whenever(driveManager.uploadBackup(any(), any())).thenReturn(Result.success(Unit))
        whenever(settingsDao.getSettings()).thenReturn(null)

        val error = repository.backupToDrive(activityContext)

        assertNull(error)
        Mockito.verify(settingsDao).insertSettings(any())
    }

    @Test
    fun backupToDrive_uploadFails_returnsMessage() = runTest {
        whenever(exporter.export()).thenReturn(sampleData)
        whenever(driveManager.uploadBackup(any(), any()))
            .thenReturn(Result.failure(GoogleDriveManager.DriveError("Koneksi ke Google Drive gagal")))

        val error = repository.backupToDrive(activityContext)

        assertEquals("Koneksi ke Google Drive gagal", error)
        Mockito.verify(settingsDao, Mockito.never()).updateLastBackupAt(any())
    }

    @Test
    fun restoreFromDrive_noBackup_returnsFriendlyMessage() = runTest {
        whenever(driveManager.downloadBackup(any())).thenReturn(Result.success(null))

        val error = repository.restoreFromDrive(activityContext)

        assertEquals("Belum ada backup di Google Drive", error)
    }

    @Test
    fun restoreFromDrive_success_returnsNull() = runTest {
        val json = serializer.serialize(sampleData)
        whenever(driveManager.downloadBackup(any())).thenReturn(Result.success(json))
        whenever(importer.import(any())).thenReturn(BackupValidation.Valid)

        val error = repository.restoreFromDrive(activityContext)

        assertNull(error)
        Mockito.verify(importer).import(json)
    }

    @Test
    fun restoreFromDrive_downloadFails_returnsMessage() = runTest {
        whenever(driveManager.downloadBackup(any()))
            .thenReturn(Result.failure(GoogleDriveManager.DriveError("Koneksi ke Google Drive gagal")))

        val error = repository.restoreFromDrive(activityContext)

        assertEquals("Koneksi ke Google Drive gagal", error)
    }

    @Test
    fun restoreFromDrive_invalidBackup_returnsValidationReason() = runTest {
        val json = serializer.serialize(sampleData.copy(schemaVersion = 99))
        whenever(driveManager.downloadBackup(any())).thenReturn(Result.success(json))
        whenever(importer.import(any()))
            .thenReturn(BackupValidation.Invalid("Versi backup (99) lebih baru dari versi aplikasi (1). Perbarui aplikasi."))

        val error = repository.restoreFromDrive(activityContext)

        assertEquals(true, error?.contains("Versi backup") == true)
    }

    @Test
    fun observeLastBackupAt_readsFromSettings() = runTest {
        whenever(settingsDao.observeSettings()).thenReturn(
            settingsFlow as Flow<SettingsEntity?>
        )
        settingsFlow.value = SettingsEntity(1, "system", false, 12345L)

        val lastBackupAt = repository.observeLastBackupAt().first()

        assertEquals(12345L, lastBackupAt)
    }

    @Test
    fun importFromUri_success_readsStreamAndImports() = runTest {
        val json = serializer.serialize(sampleData)
        val resolver = Mockito.mock(ContentResolver::class.java)
        val uri = Mockito.mock(Uri::class.java)
        whenever(activityContext.contentResolver).thenReturn(resolver)
        whenever(resolver.openInputStream(uri))
            .thenReturn(ByteArrayInputStream(json.toByteArray()))
        whenever(importer.import(any())).thenReturn(BackupValidation.Valid)

        val error = repository.importFromUri(activityContext, uri)

        assertNull(error)
        Mockito.verify(importer).import(json)
    }

    @Test
    fun importFromUri_streamFails_returnsFriendlyMessage() = runTest {
        val resolver = Mockito.mock(ContentResolver::class.java)
        val uri = Mockito.mock(Uri::class.java)
        whenever(activityContext.contentResolver).thenReturn(resolver)
        whenever(resolver.openInputStream(uri)).thenReturn(null)

        val error = repository.importFromUri(activityContext, uri)

        assertEquals("File tidak dapat dibaca", error)
        Mockito.verify(importer, Mockito.never()).import(any())
    }

    @Test
    fun importFromUri_invalidBackup_returnsValidationReason() = runTest {
        val json = serializer.serialize(sampleData)
        val resolver = Mockito.mock(ContentResolver::class.java)
        val uri = Mockito.mock(Uri::class.java)
        whenever(activityContext.contentResolver).thenReturn(resolver)
        whenever(resolver.openInputStream(uri))
            .thenReturn(ByteArrayInputStream(json.toByteArray()))
        whenever(importer.import(any()))
            .thenReturn(BackupValidation.Invalid("Backup tidak valid"))

        val error = repository.importFromUri(activityContext, uri)

        assertEquals("Backup tidak valid", error)
    }

    @Test
    fun prepareDriveConsent_alreadyAuthorized_returnsNull() = runTest {
        whenever(driveManager.authorize(any())).thenReturn("token123")

        val sender = repository.prepareDriveConsent(activityContext)

        assertNull(sender)
    }

    @Test
    fun prepareDriveConsent_needed_returnsIntentSender() = runTest {
        val intentSender = Mockito.mock(IntentSender::class.java)
        whenever(driveManager.authorize(any())).thenAnswer {
            throw GoogleDriveManager.DriveConsentRequired(intentSender)
        }

        val sender = repository.prepareDriveConsent(activityContext)

        assertEquals(intentSender, sender)
    }
}
