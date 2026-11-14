package com.myhabbit.app.data.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Schedule reminder harian pukul 20:00 via WorkManager.
 * Periodic ~24 jam dengan delay ke 20:00 berikutnya.
 */
class ReminderScheduler @Inject constructor() {

    suspend fun update(context: Context, enabled: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!enabled) {
            workManager.cancelUniqueWork(REMINDER_WORK_NAME)
            return
        }

        val now = LocalDateTime.now()
        val reminderTime = LocalTime.of(20, 0)
        var nextRun = now.with(reminderTime)
        if (!nextRun.isAfter(now)) {
            nextRun = nextRun.plusDays(1)
        }
        val initialDelay = Duration.between(now, nextRun)

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    companion object {
        const val REMINDER_WORK_NAME = "myhabbit_daily_reminder"
    }
}
