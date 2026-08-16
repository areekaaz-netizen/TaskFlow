package com.example.taskflow.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val EditTaskError = Color(0xFFDC2626)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    task: Task?,
    onBackClick: () -> Unit = {},
    onUpdateTask: (Task) -> Unit = {}
) {
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

    var reminderMinutesBefore by remember {
        mutableIntStateOf(30)
    }

    var selectedPriority by remember {
        mutableStateOf(Priority.MEDIUM)
    }

    var completed by remember {
        mutableStateOf(false)
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

    LaunchedEffect(task?.id) {
        task?.let { currentTask ->
            title = currentTask.title
            description = currentTask.description
            selectedDate = currentTask.dueDate
            selectedDateMillis = currentTask.dueDateMillis

            selectedTime = currentTask.dueTime

            val timeCalendar = Calendar.getInstance().apply {
                timeInMillis =
                    if (currentTask.dueDateTimeMillis > 0L) {
                        currentTask.dueDateTimeMillis
                    } else {
                        currentTask.dueDateMillis
                    }
            }

            selectedHour = timeCalendar.get(
                Calendar.HOUR_OF_DAY
            )

            selectedMinute = timeCalendar.get(
                Calendar.MINUTE
            )

            if (selectedTime.isBlank()) {
                selectedTime = SimpleDateFormat(
                    "h:mm a",
                    Locale.getDefault()
                ).format(timeCalendar.time)
            }

            reminderEnabled =
                currentTask.reminderEnabled

            reminderMinutesBefore =
                currentTask.reminderMinutesBefore

            selectedPriority =
                currentTask.priority

            completed =
                currentTask.completed

            titleError = false
            dateError = false
            timeError = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Task",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined.ArrowBack,
                            contentDescription =
                                "Go back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor =
                            MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor =
                            MaterialTheme.colorScheme.onSurface
                    )
            )
        }
    ) { innerPadding ->

        if (task == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.Center
            ) {
                Text(
                    text = "Loading task...",
                    fontSize = 17.sp,
                    fontWeight =
                        FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            return@Scaffold
        }

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
                text = "Update task details",
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
                    "Make your changes and save when finished.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            TaskTitleField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                    titleError = false
                }
            )

            if (titleError) {
                EditTaskErrorText(
                    message =
                        "Task title is required."
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            TaskDescriptionField(
                value = description,
                onValueChange = { newDescription ->
                    description = newDescription
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            DatePickerField(
                selectedDate = selectedDate,
                onDateSelected = { date, millis ->
                    selectedDate = date
                    selectedDateMillis = millis
                    dateError = false
                }
            )

            if (dateError) {
                EditTaskErrorText(
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
                EditTaskErrorText(
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
                onEnabledChange = { enabled ->
                    reminderEnabled = enabled
                },
                onMinutesBeforeChange = {
                        minutesBefore ->

                    reminderMinutesBefore =
                        minutesBefore
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
                        priority ->

                    selectedPriority = priority
                }
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Completed",
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "Mark this task as completed.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = completed,
                    onCheckedChange = { checked ->
                        completed = checked
                    },
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor =
                                MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor =
                                MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor =
                                MaterialTheme.colorScheme.onPrimary,
                            uncheckedTrackColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            SaveTaskButton(
                text = "Update Task",
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
                            combineEditDateAndTime(
                                dateMillis =
                                    selectedDateMillis,
                                hour =
                                    selectedHour,
                                minute =
                                    selectedMinute
                            )

                        val completedAtMillis =
                            when {
                                completed &&
                                        task.completedAtMillis == null -> {
                                    System.currentTimeMillis()
                                }

                                completed -> {
                                    task.completedAtMillis
                                }

                                else -> {
                                    null
                                }
                            }

                        onUpdateTask(
                            task.copy(
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
                                completed =
                                    completed,
                                completedAtMillis =
                                    completedAtMillis
                            )
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }
}

@Composable
private fun EditTaskErrorText(
    message: String
) {
    Spacer(
        modifier = Modifier.height(6.dp)
    )

    Text(
        text = message,
        fontSize = 13.sp,
        color = EditTaskError
    )
}

private fun combineEditDateAndTime(
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
private fun EditTaskScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        EditTaskScreen(
            task = Task(
                id = 1,
                title =
                    "Complete Android assignment",
                description =
                    "Finish the Edit Task screen.",
                dueDate =
                    "20 Jul 2026",
                dueDateMillis =
                    1784505600000L,
                dueTime =
                    "4:30 PM",
                dueDateTimeMillis =
                    1784561400000L,
                reminderEnabled =
                    true,
                reminderMinutesBefore =
                    30,
                priority =
                    Priority.HIGH,
                completed =
                    false
            )
        )
    }
}