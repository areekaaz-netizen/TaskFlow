package com.example.taskflow.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskflow.MainActivity
import com.example.taskflow.R
import java.util.Locale

object NotificationHelper {

    private const val CHANNEL_ID_PREFIX = "taskflow_reminders_v2"
    private const val CHANNEL_NAME = "Task Reminders"
    private const val CHANNEL_DESCRIPTION = "Notifications for upcoming tasks"

    private var previewPlayer: MediaPlayer? = null

    private fun getChannelId(preferences: NotificationPreferences): String {
        val soundName = preferences.notificationSound
            .trim()
            .lowercase(Locale.ROOT)
            .replace(" ", "_")

        val vibrationName =
            if (preferences.vibrationEnabled) "vibrate" else "no_vibrate"

        return "${CHANNEL_ID_PREFIX}_${soundName}_$vibrationName"
    }
    private fun getSoundUri(
        context: Context,
        selectedSound: String
    ): Uri? {
        return when (selectedSound.trim().lowercase(Locale.ROOT)) {
            "bell" ->
                Uri.parse("android.resource://${context.packageName}/${R.raw.bell}")

            "chime" ->
                Uri.parse("android.resource://${context.packageName}/${R.raw.chime}")

            "success" ->
                Uri.parse("android.resource://${context.packageName}/${R.raw.success}")

            "gentle" ->
                Uri.parse("android.resource://${context.packageName}/${R.raw.gentle}")

            "silent", "none" -> null

            else ->
                RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                )
        }
    }

    fun previewSound(
        context: Context,
        selectedSound: String
    ) {
        previewPlayer?.release()
        previewPlayer = null

        val soundUri = getSoundUri(context, selectedSound) ?: return

        previewPlayer = MediaPlayer.create(
            context.applicationContext,
            soundUri
        )?.apply {
            setOnCompletionListener { player ->
                player.release()
                if (previewPlayer === player) {
                    previewPlayer = null
                }
            }
            start()
        }
    }

    fun createNotificationChannel(context: Context): String {
        val preferences = NotificationPreferences(context)
        val channelId = getChannelId(preferences)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return channelId
        }

        val selectedSound = preferences.notificationSound
        val soundUri = getSoundUri(context, selectedSound)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channelDisplayName =
            if (
                selectedSound.equals("Silent", ignoreCase = true) ||
                selectedSound.equals("None", ignoreCase = true)
            ) {
                "$CHANNEL_NAME — Silent"
            } else {
                "$CHANNEL_NAME — $selectedSound"
            }

        val channel = NotificationChannel(
            channelId,
            channelDisplayName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableVibration(preferences.vibrationEnabled)

            if (preferences.vibrationEnabled) {
                vibrationPattern =
                    longArrayOf(0L, 250L, 150L, 250L)
            }

            if (soundUri == null) {
                setSound(null, null)
            } else {
                setSound(soundUri, audioAttributes)
            }
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        return channelId
    }

    fun showNotification(
        context: Context,
        id: Int,
        title: String,
        message: String
    ) {
        val preferences = NotificationPreferences(context)

        if (!preferences.notificationsEnabled) return

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val channelId = createNotificationChannel(context)
        val soundUri =
            getSoundUri(context, preferences.notificationSound)

        val launchIntent =
            Intent(context, MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (preferences.vibrationEnabled) {
                builder.setVibrate(
                    longArrayOf(0L, 250L, 150L, 250L)
                )
            } else {
                builder.setVibrate(longArrayOf(0L))
            }

            if (soundUri == null) {
                builder.setSilent(true)
            } else {
                builder.setSound(soundUri)
            }
        }

        try {
            val notificationManager =
                NotificationManagerCompat.from(context)

            notificationManager.cancel(id)
            notificationManager.notify(id, builder.build())
        } catch (_: SecurityException) {
            // Android 13+ notification permission was not granted.
        }
    }
}