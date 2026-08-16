package com.example.taskflow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.ui.components.BottomBar
import com.example.taskflow.ui.navigation.BottomNavItem
import com.example.taskflow.ui.theme.TaskFlowTheme
import com.example.taskflow.ui.theme.ThemeMode

private val SettingsPurple = Color(0xFF6C4FF8)
private val SettingsPurpleLight = Color(0xFFF0EBFF)

private val SettingsBlue = Color(0xFF2589F4)
private val SettingsBlueLight = Color(0xFFE4F2FF)

private val SettingsGreen = Color(0xFF27AE60)
private val SettingsGreenLight = Color(0xFFE3F7EA)

private val SettingsOrange = Color(0xFFFF8A1F)
private val SettingsOrangeLight = Color(0xFFFFEEDC)

private val SettingsPink = Color(0xFFED3A8A)
private val SettingsPinkLight = Color(0xFFFFE6F1)

@Composable
fun SettingsScreen(
    selectedTheme: ThemeMode = ThemeMode.SYSTEM,
    notificationsEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    notificationSound: String = "Default",
    defaultReminder: String = "30 min",
    snoozeDuration: String = "10 min",
    appVersion: String = "1.0.0",

    onThemeChanged: (ThemeMode) -> Unit = {},
    onAppearanceClick: () -> Unit = {},

    onNotificationsChanged: (Boolean) -> Unit = {},
    onNotificationsClick: () -> Unit = {},

    onNotificationSoundClick: () -> Unit = {},
    onNotificationSoundChanged: (String) -> Unit = {},

    onVibrationChanged: (Boolean) -> Unit = {},
    onVibrationClick: () -> Unit = {},

    onDefaultReminderClick: () -> Unit = {},
    onDefaultReminderChanged: (Int) -> Unit = {},
    onSnoozeDurationClick: () -> Unit = {},
    onSnoozeDurationChanged: (Int) -> Unit = {},

    onAboutClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},

    onTasksClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onStatisticsClick: () -> Unit = {}
) {
    val colors = settingsColors()

    var showSoundDialog by remember {
        mutableStateOf(false)
    }

    var showDefaultReminderDialog by remember {
        mutableStateOf(false)
    }

    var showSnoozeDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        bottomBar = {
            BottomBar(
                currentRoute = BottomNavItem.Settings.route,
                onTasksClick = onTasksClick,
                onCalendarClick = onCalendarClick,
                onStatsClick = onStatisticsClick,
                onSettingsClick = {}
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                SettingsHeader(
                    colors = colors
                )
            }

            item {
                SettingsSectionTitle(
                    title = "PREFERENCES"
                )
            }

            item {
                SettingsCard(
                    colors = colors
                ) {
                    SettingsSwitchRow(
                        icon = Icons.Outlined.NotificationsNone,
                        iconColor = SettingsPurple,
                        iconBackground = SettingsPurpleLight,
                        title = "Notifications",
                        subtitle = if (notificationsEnabled) {
                            "Manage your notification preferences"
                        } else {
                            "Notifications are turned off"
                        },
                        checked = notificationsEnabled,
                        colors = colors,
                        onCheckedChange = onNotificationsChanged,
                        onClick = {
                            onNotificationsChanged(!notificationsEnabled)
                            onNotificationsClick()
                        }
                    )

                    SettingsDivider(colors)

                    SettingsNavigationRow(
                        icon = Icons.Outlined.MusicNote,
                        iconColor = SettingsPink,
                        iconBackground = SettingsPinkLight,
                        title = "Notification Sound",
                        subtitle = "Choose your notification sound",
                        value = notificationSound,
                        colors = colors,
                        enabled = notificationsEnabled,
                        onClick = {
                            onNotificationSoundClick()
                            showSoundDialog = true
                        }
                    )

                    SettingsDivider(colors)

                    SettingsSwitchRow(
                        icon = Icons.Outlined.Vibration,
                        iconColor = SettingsPurple,
                        iconBackground = SettingsPurpleLight,
                        title = "Vibration",
                        subtitle = "Vibrate for notifications",
                        checked = vibrationEnabled,
                        enabled = notificationsEnabled,
                        colors = colors,
                        onCheckedChange = onVibrationChanged,
                        onClick = {
                            onVibrationChanged(!vibrationEnabled)
                            onVibrationClick()
                        }
                    )

                    SettingsDivider(colors)

                    SettingsNavigationRow(
                        icon = Icons.Outlined.Schedule,
                        iconColor = SettingsBlue,
                        iconBackground = SettingsBlueLight,
                        title = "Default Reminder Time",
                        subtitle = "Set default reminder time",
                        value = defaultReminder,
                        colors = colors,
                        enabled = notificationsEnabled,
                        onClick = {
                            onDefaultReminderClick()
                            showDefaultReminderDialog = true
                        }
                    )

                    SettingsDivider(colors)

                    SettingsNavigationRow(
                        icon = Icons.Outlined.Snooze,
                        iconColor = SettingsPink,
                        iconBackground = SettingsPinkLight,
                        title = "Snooze Duration",
                        subtitle = "Set how long to snooze",
                        value = snoozeDuration,
                        colors = colors,
                        enabled = notificationsEnabled,
                        onClick = {
                            onSnoozeDurationClick()
                            showSnoozeDialog = true
                        }
                    )
                }
            }

            item {
                SettingsSectionTitle(
                    title = "APPEARANCE"
                )
            }

            item {
                AppearanceSettingsCard(
                    selectedTheme = selectedTheme,
                    colors = colors,
                    onThemeChanged = onThemeChanged,
                    onAppearanceClick = onAppearanceClick
                )
            }

            item {
                SettingsSectionTitle(
                    title = "ABOUT"
                )
            }

            item {
                SettingsCard(
                    colors = colors
                ) {
                    SettingsNavigationRow(
                        icon = Icons.Outlined.Info,
                        iconColor = SettingsBlue,
                        iconBackground = SettingsBlueLight,
                        title = "About TaskFlow",
                        subtitle = "Learn more about the app",
                        colors = colors,
                        onClick = onAboutClick
                    )

                    SettingsDivider(colors)

                    SettingsNavigationRow(
                        icon = Icons.Outlined.Shield,
                        iconColor = SettingsGreen,
                        iconBackground = SettingsGreenLight,
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        colors = colors,
                        onClick = onPrivacyPolicyClick
                    )

                    SettingsDivider(colors)

                    SettingsNavigationRow(
                        icon = Icons.Outlined.HelpOutline,
                        iconColor = SettingsOrange,
                        iconBackground = SettingsOrangeLight,
                        title = "Help & Support",
                        subtitle = "Get help and contact support",
                        colors = colors,
                        onClick = onHelpClick
                    )
                }
            }

            item {
                AppVersionCard(
                    version = appVersion,
                    colors = colors
                )
            }
        }
    }

    if (showSoundDialog) {
        SettingsChoiceDialog(
            title = "Notification Sound",
            options = listOf(
                "Default",
                "Bell",
                "Chime",
                "Success",
                "Gentle",
                "Silent"
            ),
            selectedOption = notificationSound,
            onOptionSelected = { selected ->
                onNotificationSoundChanged(selected)
            },
            onDismiss = {
                showSoundDialog = false
            }
        )
    }

    if (showDefaultReminderDialog) {
        val reminderOptions = listOf(
            5 to "5 min",
            10 to "10 min",
            15 to "15 min",
            30 to "30 min",
            60 to "1 hour"
        )

        SettingsChoiceDialog(
            title = "Default Reminder Time",
            options = reminderOptions.map { it.second },
            selectedOption = defaultReminder,
            onOptionSelected = { selected ->
                reminderOptions
                    .firstOrNull { it.second == selected }
                    ?.let { option ->
                        onDefaultReminderChanged(option.first)
                    }

                showDefaultReminderDialog = false
            },
            onDismiss = {
                showDefaultReminderDialog = false
            }
        )
    }

    if (showSnoozeDialog) {
        val snoozeOptions = listOf(
            5 to "5 min",
            10 to "10 min",
            15 to "15 min",
            30 to "30 min"
        )

        SettingsChoiceDialog(
            title = "Snooze Duration",
            options = snoozeOptions.map { it.second },
            selectedOption = snoozeDuration,
            onOptionSelected = { selected ->
                snoozeOptions
                    .firstOrNull { it.second == selected }
                    ?.let { option ->
                        onSnoozeDurationChanged(option.first)
                    }

                showSnoozeDialog = false
            },
            onDismiss = {
                showSnoozeDialog = false
            }
        )
    }
}

@Composable
private fun SettingsChoiceDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = {
                                onOptionSelected(option)
                            }
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = option,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun SettingsHeader(
    colors: SettingsColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Settings",
                fontSize = 28.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colors.primaryText
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )
            Text(
                text = "Customize your experience",
                fontSize = 13.sp,
                color = colors.secondaryText
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            SettingsPurpleLight,
                            Color(0xFFE8DEFF)
                        )
                    ),
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = SettingsPurple,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(
    title: String
) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = SettingsPurple
    )
}

@Composable
private fun SettingsCard(
    colors: SettingsColors,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colors.border
        ),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}
@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    colors: SettingsColors,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(
            icon = icon,
            iconColor = iconColor,
            iconBackground = iconBackground
        )

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) {
                    colors.primaryText
                } else {
                    colors.secondaryText
                }
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                color = colors.secondaryText
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SettingsPurple,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = colors.switchUnchecked,
                disabledCheckedThumbColor = Color.White.copy(
                    alpha = 0.75f
                ),
                disabledCheckedTrackColor = SettingsPurple.copy(
                    alpha = 0.40f
                ),
                disabledUncheckedThumbColor = Color.White.copy(
                    alpha = 0.75f
                ),
                disabledUncheckedTrackColor =
                    colors.switchUnchecked.copy(
                        alpha = 0.45f
                    )
            )
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    title: String,
    subtitle: String,
    colors: SettingsColors,
    value: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIcon(
            icon = icon,
            iconColor = iconColor,
            iconBackground = iconBackground
        )

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) {
                    colors.primaryText
                } else {
                    colors.secondaryText.copy(alpha = 0.60f)
                }
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                color = colors.secondaryText
            )
        }

        if (!value.isNullOrBlank()) {
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) {
                    SettingsPurple
                } else {
                    colors.secondaryText.copy(alpha = 0.60f)
                }
            )

            Spacer(
                modifier = Modifier.width(7.dp)
            )
        }

        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = if (enabled) {
                colors.secondaryText
            } else {
                colors.secondaryText.copy(alpha = 0.45f)
            },
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
private fun SettingsIcon(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color
) {
    Box(
        modifier = Modifier
            .size(39.dp)
            .background(
                color = iconBackground,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Composable
private fun SettingsDivider(
    colors: SettingsColors
) {
    HorizontalDivider(
        modifier = Modifier.padding(
            start = 62.dp
        ),
        color = colors.border
    )
}

@Composable
private fun AppearanceSettingsCard(
    selectedTheme: ThemeMode,
    colors: SettingsColors,
    onThemeChanged: (ThemeMode) -> Unit,
    onAppearanceClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onAppearanceClick
            ),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colors.border
        ),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 11.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIcon(
                icon = Icons.Outlined.PhoneAndroid,
                iconColor = SettingsGreen,
                iconBackground = SettingsGreenLight
            )

            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "App Appearance",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Choose theme",
                    fontSize = 10.sp,
                    color = colors.secondaryText
                )
            }

            CompactThemeButton(
                icon = Icons.Outlined.LightMode,
                selected = selectedTheme == ThemeMode.LIGHT,
                colors = colors,
                contentDescription = "Light theme",
                onClick = {
                    onThemeChanged(ThemeMode.LIGHT)
                }
            )

            Spacer(
                modifier = Modifier.width(5.dp)
            )

            CompactThemeButton(
                icon = Icons.Outlined.DarkMode,
                selected = selectedTheme == ThemeMode.DARK,
                colors = colors,
                contentDescription = "Dark theme",
                onClick = {
                    onThemeChanged(ThemeMode.DARK)
                }
            )

            Spacer(
                modifier = Modifier.width(5.dp)
            )

            CompactThemeButton(
                icon = Icons.Outlined.PhoneAndroid,
                selected = selectedTheme == ThemeMode.SYSTEM,
                colors = colors,
                contentDescription = "System theme",
                onClick = {
                    onThemeChanged(ThemeMode.SYSTEM)
                }
            )
        }
    }
}

@Composable
private fun CompactThemeButton(
    icon: ImageVector,
    selected: Boolean,
    colors: SettingsColors,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(31.dp)
            .background(
                color = if (selected) {
                    SettingsPurpleLight
                } else {
                    colors.optionBackground
                },
                shape = RoundedCornerShape(9.dp)
            )
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) {
                SettingsPurple
            } else {
                colors.secondaryText
            },
            modifier = Modifier.size(17.dp)
        )

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(8.dp)
                    .background(
                        color = SettingsPurple,
                        shape = CircleShape
                    )
            )
        }
    }
}
@Composable
private fun AppVersionCard(
    version: String,
    colors: SettingsColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.versionBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = colors.versionBadge,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 5.dp
                    )
            ) {
                Text(
                    text = "v$version",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = SettingsPurple
                )
            }

            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Column {
                Text(
                    text = "App Version",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "You are using the latest version",
                    fontSize = 10.sp,
                    color = colors.secondaryText
                )
            }
        }
    }
}
private data class SettingsColors(
    val background: Color,
    val card: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val border: Color,
    val optionBackground: Color,
    val versionBackground: Color,
    val versionBadge: Color,
    val switchUnchecked: Color
)
@Composable
private fun settingsColors(): SettingsColors {
    val colorScheme = MaterialTheme.colorScheme

    return SettingsColors(
        background = colorScheme.background,
        card = colorScheme.surface,
        primaryText = colorScheme.onSurface,
        secondaryText = colorScheme.onSurfaceVariant,
        border = colorScheme.outline.copy(
            alpha = 0.45f
        ),
        optionBackground = colorScheme.surfaceVariant,
        versionBackground = colorScheme.primaryContainer,
        versionBadge = colorScheme.surface.copy(
            alpha = 0.85f
        ),
        switchUnchecked = colorScheme.outline.copy(
            alpha = 0.60f
        )
    )
}
@Preview(
    name = "Settings Light",
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun SettingsScreenLightPreview() {
    var themeMode by remember {
        mutableStateOf(ThemeMode.LIGHT)
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var vibrationEnabled by remember {
        mutableStateOf(true)
    }

    TaskFlowTheme(
        themeMode = ThemeMode.LIGHT,
        dynamicColor = false
    ) {
        SettingsScreen(
            selectedTheme = themeMode,
            notificationsEnabled = notificationsEnabled,
            vibrationEnabled = vibrationEnabled,
            onThemeChanged = {
                themeMode = it
            },
            onNotificationsChanged = {
                notificationsEnabled = it
            },
            onVibrationChanged = {
                vibrationEnabled = it
            }
        )
    }
}

@Preview(
    name = "Settings Dark",
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun SettingsScreenDarkPreview() {
    var themeMode by remember {
        mutableStateOf(ThemeMode.DARK)
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var vibrationEnabled by remember {
        mutableStateOf(true)
    }

    TaskFlowTheme(
        themeMode = ThemeMode.DARK,
        dynamicColor = false
    ) {
        SettingsScreen(
            selectedTheme = themeMode,
            notificationsEnabled = notificationsEnabled,
            vibrationEnabled = vibrationEnabled,
            onThemeChanged = {
                themeMode = it
            },
            onNotificationsChanged = {
                notificationsEnabled = it
            },
            onVibrationChanged = {
                vibrationEnabled = it
            }
        )
    }
}