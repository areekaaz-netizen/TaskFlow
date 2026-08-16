package com.example.taskflow.notifications

import android.content.Context

class NotificationPreferences(context: Context) {

    companion object {

        private const val PREF_NAME =
            "taskflow_preferences"

        private const val KEY_NOTIFICATIONS =
            "notifications_enabled"

        private const val KEY_VIBRATION =
            "vibration_enabled"

        private const val KEY_DEFAULT_REMINDER =
            "default_reminder"

        private const val KEY_NOTIFICATION_SOUND =
            "notification_sound"

        private const val KEY_SNOOZE =
            "snooze_duration"

        const val SOUND_DEFAULT = "Default"
        const val SOUND_BELL = "Bell"
        const val SOUND_CHIME = "Chime"
        const val SOUND_SUCCESS = "Success"
        const val SOUND_GENTLE = "Gentle"
        const val SOUND_SILENT = "Silent"

        val AVAILABLE_SOUNDS = listOf(
            SOUND_DEFAULT,
            SOUND_BELL,
            SOUND_CHIME,
            SOUND_SUCCESS,
            SOUND_GENTLE,
            SOUND_SILENT
        )
    }

    private val prefs =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(
            KEY_NOTIFICATIONS,
            true
        )
        set(value) {
            prefs.edit()
                .putBoolean(
                    KEY_NOTIFICATIONS,
                    value
                )
                .apply()
        }

    var vibrationEnabled: Boolean
        get() = prefs.getBoolean(
            KEY_VIBRATION,
            true
        )
        set(value) {
            prefs.edit()
                .putBoolean(
                    KEY_VIBRATION,
                    value
                )
                .apply()
        }

    var defaultReminder: Int
        get() = prefs.getInt(
            KEY_DEFAULT_REMINDER,
            30
        )
        set(value) {
            prefs.edit()
                .putInt(
                    KEY_DEFAULT_REMINDER,
                    value
                )
                .apply()
        }

    var notificationSound: String
        get() = prefs.getString(
            KEY_NOTIFICATION_SOUND,
            SOUND_DEFAULT
        ) ?: SOUND_DEFAULT
        set(value) {
            val safeValue =
                if (value in AVAILABLE_SOUNDS) value
                else SOUND_DEFAULT

            prefs.edit()
                .putString(
                    KEY_NOTIFICATION_SOUND,
                    safeValue
                )
                .apply()
        }

    var snoozeDuration: Int
        get() = prefs.getInt(
            KEY_SNOOZE,
            10
        )
        set(value) {
            prefs.edit()
                .putInt(
                    KEY_SNOOZE,
                    value
                )
                .apply()
        }
}