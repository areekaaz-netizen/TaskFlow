package com.example.taskflow.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.taskflow.model.Task
import java.util.concurrent.TimeUnit

class ReminderScheduler(context: Context) {

    private val appContext = context.applicationContext
    private val workManager = WorkManager.getInstance(appContext)
    private val preferences = NotificationPreferences(appContext)

    fun scheduleReminder(task: Task) {
        cancelReminder(task.id)

        if (!preferences.notificationsEnabled ||
            !task.reminderEnabled ||
            task.completed ||
            task.id <= 0
        ) {
            return
        }

        val reminderTimeMillis = task.dueDateTimeMillis -
                TimeUnit.MINUTES.toMillis(task.reminderMinutesBefore.toLong())

        val delayMillis = reminderTimeMillis - System.currentTimeMillis()
        if (delayMillis <= 0L) return

        val inputData = Data.Builder()
            .putInt(ReminderWorker.KEY_TASK_ID, task.id)
            .putString(ReminderWorker.KEY_TASK_TITLE, task.title)
            .putString(ReminderWorker.KEY_TASK_DESCRIPTION, task.description)
            .putLong(ReminderWorker.KEY_DUE_TIME, task.dueDateTimeMillis)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(TAG_ALL_REMINDERS)
            .addTag(taskTag(task.id))
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName(task.id),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminder(taskId: Int) {
        if (taskId <= 0) return
        workManager.cancelUniqueWork(uniqueWorkName(taskId))
    }

    fun cancelAllReminders() {
        workManager.cancelAllWorkByTag(TAG_ALL_REMINDERS)
    }

    private fun uniqueWorkName(taskId: Int) = "taskflow_reminder_$taskId"
    private fun taskTag(taskId: Int) = "taskflow_task_$taskId"

    companion object {
        private const val TAG_ALL_REMINDERS = "taskflow_all_reminders"
    }
}