package com.myhabbit.app.data.backup

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Serialisasi backup JSON (ARCHITECTURE.md §16).
 */
class BackupSerializer @Inject constructor() {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun serialize(data: BackupData): String {
        return json.encodeToString(data)
    }

    fun deserialize(jsonString: String): BackupData? {
        return try {
            json.decodeFromString<BackupData>(jsonString)
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}
