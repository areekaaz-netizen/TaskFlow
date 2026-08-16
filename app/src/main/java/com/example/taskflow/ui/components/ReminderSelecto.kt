package com.example.taskflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ReminderPurple =
    Color(0xFF5B5FEF)

private val ReminderDark =
    Color(0xFF20243A)

private val ReminderGray =
    Color(0xFF737789)

private val ReminderBorder =
    Color(0xFFD9DAE5)

data class ReminderOption(
    val title: String,
    val minutesBefore: Int
)

private val reminderOptions = listOf(
    ReminderOption(
        title = "At due time",
        minutesBefore = 0
    ),
    ReminderOption(
        title = "10 minutes before",
        minutesBefore = 10
    ),
    ReminderOption(
        title = "30 minutes before",
        minutesBefore = 30
    ),
    ReminderOption(
        title = "1 hour before",
        minutesBefore = 60
    ),
    ReminderOption(
        title = "1 day before",
        minutesBefore = 1440
    )
)

@Composable
fun ReminderSelector(
    enabled: Boolean,
    minutesBefore: Int,
    onEnabledChange: (Boolean) -> Unit,
    onMinutesBeforeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedOption =
        reminderOptions.firstOrNull {
            it.minutesBefore ==
                    minutesBefore
        } ?: reminderOptions[2]

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Reminder",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ReminderDark
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text = if (enabled) {
                        "Notify me before the deadline."
                    } else {
                        "Reminder is turned off."
                    },
                    fontSize = 13.sp,
                    color = ReminderGray
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange =
                    onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor =
                        Color.White,
                    checkedTrackColor =
                        ReminderPurple,
                    uncheckedThumbColor =
                        Color.White,
                    uncheckedTrackColor =
                        Color(0xFFD1D5DB)
                )
            )
        }

        if (enabled) {
            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clickable {
                        expanded = true
                    },
                shape =
                    RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = ReminderBorder
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 15.dp,
                            vertical = 15.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedOption.title,
                        modifier =
                            Modifier.weight(1f),
                        fontSize = 14.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color = ReminderDark
                    )

                    Icon(
                        imageVector =
                            Icons.Outlined
                                .KeyboardArrowDown,
                        contentDescription =
                            "Select reminder time",
                        tint = ReminderPurple
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                reminderOptions.forEach {
                        option ->

                    DropdownMenuItem(
                        text = {
                            Text(option.title)
                        },
                        onClick = {
                            onMinutesBeforeChange(
                                option.minutesBefore
                            )
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}