package com.example.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskflow.notifications.NotificationHelper
import com.example.taskflow.notifications.NotificationPermission
import com.example.taskflow.ui.navigation.AppNavigation
import com.example.taskflow.ui.theme.TaskFlowTheme
import com.example.taskflow.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    private lateinit var notificationPermission:NotificationPermission

    override fun onCreate(savedInstanceState: Bundle?) {super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // Creates the TaskFlow reminder notification channel.
        NotificationHelper.createNotificationChannel(this)

        // Handles notification permission on Android 13 and newer.
        notificationPermission = NotificationPermission(this)

        // Show the Android notification permission popup.
        notificationPermission.requestPermission()

        setContent {
            val themeMode by themeViewModel
                .themeMode
                .collectAsStateWithLifecycle()

            TaskFlowTheme(
                themeMode = themeMode,
                dynamicColor = false
            ) {
                AppNavigation(
                    themeMode = themeMode,
                    onThemeChanged =
                        themeViewModel::updateThemeMode
                )
            }
        }
    }
}