package com.myhabbit.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.myhabbit.app.data.notification.ReminderScheduler
import com.myhabbit.app.domain.repository.SettingsRepository
import com.myhabbit.app.presentation.navigation.AppNavigation
import com.myhabbit.app.ui.theme.MyHabbitTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeFlow = remember { settingsRepository.observeTheme() }
            val theme by themeFlow.collectAsStateWithLifecycle(initialValue = "system")

            // PRD.md §49: Light / Dark / System Default (default system)
            val darkTheme = when (theme) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            MyHabbitTheme(darkTheme = darkTheme) {
                AppNavigation()
            }
        }

        // Pastikan reminder aktif bila diaktifkan (PRD.md §48)
        lifecycleScope.launch {
            try {
                val enabled = settingsRepository.observeNotificationsEnabled().first()
                reminderScheduler.update(this@MainActivity, enabled)
            } catch (_: Exception) {
                // Scheduler gagal — jangan crash app
            }
        }
    }
}
