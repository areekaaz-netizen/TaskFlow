package com.example.taskflow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.ui.navigation.BottomNavItem

@Composable
fun BottomBar(
    currentRoute: String,
    onTasksClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    item = BottomNavItem.Tasks,
                    selected = currentRoute == BottomNavItem.Tasks.route,
                    onClick = onTasksClick
                )

                BottomBarItem(
                    item = BottomNavItem.Calendar,
                    selected = currentRoute == BottomNavItem.Calendar.route,
                    onClick = onCalendarClick
                )

                BottomBarItem(
                    item = BottomNavItem.Statistics,
                    selected = currentRoute == BottomNavItem.Statistics.route,
                    onClick = onStatsClick
                )

                BottomBarItem(
                    item = BottomNavItem.Settings,
                    selected = currentRoute == BottomNavItem.Settings.route,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            },
            color = itemColor
        )
    }
}