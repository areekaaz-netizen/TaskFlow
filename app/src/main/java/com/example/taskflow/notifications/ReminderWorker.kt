package com.example.taskflow.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.text.DateFormat
import java.util.Date

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val preferences = NotificationPreferences(applicationContext)
        if (!preferences.notificationsEnabled) return Result.success()

        val taskId = inputData.getInt(KEY_TASK_ID, -1)
        val title = inputData.getString(KEY_TASK_TITLE).orEmpty()
        val description = inputData.getString(KEY_TASK_DESCRIPTION).orEmpty()
        val dueTime = inputData.getLong(KEY_DUE_TIME, 0L)

        if (taskId <= 0 || title.isBlank()) return Result.failure()

        val message = when {
            description.isNotBlank() -> description
            dueTime > 0L -> "Due ${DateFormat.getDateTimeInstance().format(Date(dueTime))}"
            else -> "Your task is due soon."
        }

        NotificationHelper.createNotificationChannel(applicationContext)

        NotificationHelper.showNotification(
            context = applicationContext,
            id = taskId,
            title = title,
            message = message
        )

        return Result.success()
    }

    companion object {
        const val KEY_TASK_ID = "task_id"
        const val KEY_TASK_TITLE = "task_title"
        const val KEY_TASK_DESCRIPTION = "task_description"
        const val KEY_DUE_TIME = "due_time"
    }
}