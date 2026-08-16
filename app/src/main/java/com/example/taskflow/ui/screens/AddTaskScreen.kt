package com.example.taskflow.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
import com.example.taskflow.model.Task
import com.example.taskflow.ui.components.DatePickerField
import com.example.taskflow.ui.components.PrioritySelector
import com.example.taskflow.ui.components.ReminderSelector
import com.example.taskflow.ui.components.SaveTaskButton
import com.example.taskflow.ui.components.TaskDescriptionField
import com.example.taskflow.ui.components.TaskTitleField
import com.example.taskflow.ui.components.TimePickerField
import com.example.taskflow.ui.theme.TaskFlowTheme
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import com.example.taskflow.notifications.NotificationPreferences
import androidx.compose.runtime.saveable.rememberSaveable

private val AddTaskError =
    Color(0xFFDC2626)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBackClick: () -> Unit = {},
    onSaveTask: (Task) -> Unit = {}
) {
    val context = LocalContext.current

    val notificationPreferences = remember {
        NotificationPreferences(context.applicationContext)
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedDateMillis by remember {
        mutableLongStateOf(0L)
    }

    var selectedTime by remember {
        mutableStateOf("")
    }

    var selectedHour by remember {
        mutableIntStateOf(-1)
    }
    var selectedMinute by remember {
        mutableIntStateOf(-1)
    }
    var reminderEnabled by remember {
        mutableStateOf(false)
    }

    var reminderMinutesBefore by rememberSaveable {
        mutableIntStateOf(
            notificationPreferences.defaultReminder
        )
    }

    var selectedPriority by remember {
        mutableStateOf(Priority.MEDIUM)
    }

    var titleError by remember {
        mutableStateOf(false)
    }

    var dateError by remember {
        mutableStateOf(false)
    }

    var timeError by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Task",
                        fontSize = 21.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .ArrowBack,
                            contentDescription =
                                "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 22.dp,
                    vertical = 20.dp
                )
        ) {
            Text(
                text = "Create a new task",
                fontSize = 25.sp,
                fontWeight =
                    FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )
            Text(
                text =
                    "Add the task details below.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            TaskTitleField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                }
            )

            if (titleError) {
                ErrorText(
                    message =
                        "Task title is required."
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            TaskDescriptionField(
                value = description,
                onValueChange = {
                    description = it
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            DatePickerField(
                selectedDate = selectedDate,
                onDateSelected = {
                        date,
                        millis ->

                    selectedDate = date
                    selectedDateMillis = millis
                    dateError = false
                }
            )

            if (dateError) {
                ErrorText(
                    message =
                        "Please select a due date."
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            TimePickerField(
                selectedTime = selectedTime,
                onTimeSelected = {
                        time,
                        hour,
                        minute ->

                    selectedTime = time
                    selectedHour = hour
                    selectedMinute = minute
                    timeError = false
                }
            )

            if (timeError) {
                ErrorText(
                    message =
                        "Please select a due time."
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            ReminderSelector(
                enabled = reminderEnabled,
                minutesBefore =
                    reminderMinutesBefore,
                onEnabledChange = {
                    reminderEnabled = it
                },
                onMinutesBeforeChange = {
                    reminderMinutesBefore = it
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Priority",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            PrioritySelector(
                selectedPriority =
                    selectedPriority,
                onPrioritySelected = {
                    selectedPriority = it
                }
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            SaveTaskButton(
                text = "Save Task",
                onClick = {
                    val cleanedTitle =
                        title.trim()

                    val validTitle =
                        cleanedTitle.isNotEmpty()

                    val validDate =
                        selectedDate.isNotBlank() &&
                                selectedDateMillis > 0L

                    val validTime =
                        selectedTime.isNotBlank() &&
                                selectedHour >= 0 &&
                                selectedMinute >= 0

                    titleError = !validTitle
                    dateError = !validDate
                    timeError = !validTime

                    if (
                        validTitle &&
                        validDate &&
                        validTime
                    ) {
                        val dueDateTimeMillis =
                            combineDateAndTime(
                                dateMillis =
                                    selectedDateMillis,
                                hour =
                                    selectedHour,
                                minute =
                                    selectedMinute
                            )

                        onSaveTask(
                            Task(
                                title =
                                    cleanedTitle,
                                description =
                                    description.trim(),
                                dueDate =
                                    selectedDate,
                                dueDateMillis =
                                    selectedDateMillis,
                                dueTime =
                                    selectedTime,
                                dueDateTimeMillis =
                                    dueDateTimeMillis,
                                reminderEnabled =
                                    reminderEnabled,
                                reminderMinutesBefore =
                                    reminderMinutesBefore,
                                priority =
                                    selectedPriority,
                                completed = false
                            )
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }
    }
}

@Composable
private fun ErrorText(
    message: String
) {
    Spacer(
        modifier = Modifier.height(6.dp)
    )

    Text(
        text = message,
        fontSize = 13.sp,
        color = AddTaskError
    )
}

private fun combineDateAndTime(
    dateMillis: Long,
    hour: Int,
    minute: Int
): Long {
    return Calendar.getInstance().apply {
        timeInMillis = dateMillis

        set(
            Calendar.HOUR_OF_DAY,
            hour
        )

        set(
            Calendar.MINUTE,
            minute
        )

        set(
            Calendar.SECOND,
            0
        )

        set(
            Calendar.MILLISECOND,
            0
        )
    }.timeInMillis
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun AddTaskScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        AddTaskScreen()
    }
}