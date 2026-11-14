package com.myhabbit.app.data.drive

import android.content.Context
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Tasks
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Backup file di Drive (PRD.md §32, ARCHITECTURE.md §15):
 *
 * MyHabbit/
 *   myhabbit-backup.json
 *
 * Permission minimal: scope `drive.file` — aplikasi hanya boleh
 * mengelola file yang ia buat sendiri.
 *
 * Konvensi hasil:
 * - `null` berarti "belum ada" (file/folder di Drive)
 * - error lain dibungkus [DriveError]
 */
class GoogleDriveManager @Inject constructor() {

    class DriveError(message: String, cause: Throwable? = null) : Exception(message, cause)

    companion object {
        private const val DRIVE_FILE_SCOPE = "https://www.googleapis.com/auth/drive.file"
        private const val BACKUP_FOLDER_NAME = "MyHabbit"
        private const val BACKUP_FILE_NAME = "myhabbit-backup.json"
        private const val FOLDER_MIME = "application/vnd.google-apps.folder"
        private const val JSON_MIME = "application/json"
    }

    private val transport: HttpTransport by lazy {
        try {
            GoogleNetHttpTransport.newTrustedTransport()
        } catch (e: Exception) {
            throw DriveError("Gagal menyiapkan transport network", e)
        }
    }

    private val jsonFactory = GsonFactory.getDefaultInstance()

    /**
     * Minta otorisasi scope drive.file (ARCHITECTURE.md §14:
     * terpisah dari sign-in). Return access token, atau lempar [DriveError].
     */
    suspend fun authorize(activityContext: Context): String {
        return withContext(Dispatchers.IO) {
            try {
                val request = AuthorizationRequest.Builder()
                    .setRequestedScopes(listOf(Scope(DRIVE_FILE_SCOPE)))
                    .build()
                val result = Tasks.await(
                    Identity.getAuthorizationClient(activityContext)
                        .authorize(request)
                )
                val token = result.accessToken
                if (token.isNullOrBlank()) {
                    throw DriveError("Otorisasi Drive gagal atau dibatalkan")
                }
                token
            } catch (e: DriveError) {
                throw e
            } catch (e: Exception) {
                throw DriveError("Gagal mengakses Google Drive: ${e.message ?: "unknown"}", e)
            }
        }
    }

    /** Upload backup; file dibuat bila belum ada, di-update bila sudah ada. */
    suspend fun uploadBackup(activityContext: Context, jsonString: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val drive = buildDrive(authorize(activityContext))
                val folderId = getOrCreateFolder(drive)
                val existingFileId = findBackupFile(drive, folderId)
                val content = ByteArrayContent(JSON_MIME, jsonString.toByteArray())
                val metadata = File()
                    .setName(BACKUP_FILE_NAME)
                    .setParents(listOf(folderId))

                if (existingFileId != null) {
                    drive.files().update(existingFileId, metadata, content).execute()
                } else {
                    drive.files().create(metadata, content).execute()
                }
                Result.success(Unit)
            } catch (e: DriveError) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(DriveError("Koneksi ke Google Drive gagal: ${e.message ?: "unknown"}", e))
            } catch (e: Exception) {
                Result.failure(DriveError("Upload gagal: ${e.message ?: "unknown"}", e))
            }
        }
    }

    /** Download backup JSON; null bila file belum ada di Drive. */
    suspend fun downloadBackup(activityContext: Context): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val drive = buildDrive(authorize(activityContext))
                val folderId = findFolder(drive) ?: return@withContext Result.success(null)
                val fileId = findBackupFile(drive, folderId)
                    ?: return@withContext Result.success(null)
                val content = drive.files().get(fileId)
                    .executeMediaAsInputStream()
                    .bufferedReader()
                    .use { it.readText() }
                Result.success(content)
            } catch (e: DriveError) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(DriveError("Koneksi ke Google Drive gagal: ${e.message ?: "unknown"}", e))
            } catch (e: Exception) {
                Result.failure(DriveError("Download gagal: ${e.message ?: "unknown"}", e))
            }
        }
    }

    private fun buildDrive(accessToken: String): Drive {
        val initializer = HttpRequestInitializer { request ->
            request.headers.authorization = "Bearer $accessToken"
        }
        return Drive.Builder(transport, jsonFactory, initializer)
            .setApplicationName("MyHabbit")
            .build()
    }

    private fun findFolder(drive: Drive): String? {
        val result: FileList = drive.files().list()
            .setQ(
                "name = '$BACKUP_FOLDER_NAME' and " +
                    "mimeType = '$FOLDER_MIME' and trashed = false"
            )
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()
        return result.files?.firstOrNull()?.id
    }

    private fun getOrCreateFolder(drive: Drive): String {
        findFolder(drive)?.let { return it }
        val folder = drive.files().create(
            File().setName(BACKUP_FOLDER_NAME).setMimeType(FOLDER_MIME)
        ).setFields("id").execute()
        return folder.id
    }

    private fun findBackupFile(drive: Drive, folderId: String): String? {
        val result: FileList = drive.files().list()
            .setQ(
                "name = '$BACKUP_FILE_NAME' and " +
                    "'$folderId' in parents and trashed = false"
            )
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()
        return result.files?.firstOrNull()?.id
    }
}
