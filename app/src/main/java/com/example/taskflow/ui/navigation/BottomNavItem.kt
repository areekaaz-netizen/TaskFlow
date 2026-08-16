package com.example.taskflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Tasks : BottomNavItem(
        route = "home",
        title = "Tasks",
        icon = Icons.Outlined.Checklist
    )

    object Calendar : BottomNavItem(
        route = "calendar",
        title = "Calendar",
        icon = Icons.Outlined.CalendarMonth
    )

    object Statistics : BottomNavItem(
        route = "statistics",
        title = "Stats",
        icon = Icons.Outlined.BarChart
    )

    object Settings : BottomNavItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Outlined.Settings
    )

    companion object {

        val items = listOf(
            Tasks,
            Calendar,
            Statistics,
            Settings
        )

    }

}