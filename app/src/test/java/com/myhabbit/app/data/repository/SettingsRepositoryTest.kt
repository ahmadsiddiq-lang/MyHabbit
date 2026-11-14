package com.myhabbit.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myhabbit.app.data.local.database.MyHabbitDatabase
import kotlinx.coroutines.flow.first
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
class SettingsRepositoryTest {

    private lateinit var database: MyHabbitDatabase
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, MyHabbitDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SettingsRepositoryImpl(database.settingsDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun defaults_areSystemThemeAndNotificationsOff() = runTest {
        assertEquals("system", repository.observeTheme().first())
        assertEquals(false, repository.observeNotificationsEnabled().first())
    }

    @Test
    fun setTheme_persists() = runTest {
        repository.setTheme("dark")
        assertEquals("dark", repository.observeTheme().first())

        repository.setTheme("light")
        assertEquals("light", repository.observeTheme().first())

        repository.setTheme("system")
        assertEquals("system", repository.observeTheme().first())
    }

    @Test
    fun setTheme_persistsEvenWhenSettingsRowNotFound() = runTest {
        // Simulasi database fresh tanpa settings row
        database.settingsDao().deleteSettings()

        repository.setTheme("dark")

        assertEquals("dark", repository.observeTheme().first())
    }

    @Test
    fun setNotificationsEnabled_persists() = runTest {
        repository.setNotificationsEnabled(true)
        assertEquals(true, repository.observeNotificationsEnabled().first())

        repository.setNotificationsEnabled(false)
        assertEquals(false, repository.observeNotificationsEnabled().first())
    }
}
