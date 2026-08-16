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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
import com.example.taskflow.model.Task
import com.example.taskflow.ui.components.BottomBar
import com.example.taskflow.ui.navigation.BottomNavItem
import com.example.taskflow.ui.theme.TaskFlowTheme
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material3.MaterialTheme

private val CalendarGreen = Color(0xFF16A34A)
private val CalendarGreenBackground = Color(0xFFE5F7EA)
private val CalendarOrange = Color(0xFFF59E0B)
private val CalendarOrangeBackground = Color(0xFFFFF3DC)
private val CalendarRed = Color(0xFFDC2626)
private val CalendarRedBackground = Color(0xFFFFE8E8)

@Composable
fun CalendarScreen(
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit = {},
    onTasksClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val currentTimeMillis = System.currentTimeMillis()

    var displayedMonth by remember {
        mutableStateOf(YearMonth.from(today))
    }

    var selectedDate by remember {
        mutableStateOf(today)
    }

    val tasksByDate = remember(tasks) {
        tasks.groupBy { task ->
            task.toLocalDate(zoneId)
        }
    }

    val selectedDateTasks = remember(tasks, selectedDate) {
        tasks
            .filter { task ->
                task.toLocalDate(zoneId) == selectedDate
            }
            .sortedBy { task ->
                task.validDueMillis()
            }
    }
    val todayTasks = remember(tasks, today) {
        tasks.filter { task ->
            task.toLocalDate(zoneId) == today
        }
    }

    val todayCompleted = todayTasks.count { task ->
        task.completed
    }

    val todayPending = todayTasks.count { task ->
        !task.completed &&
                task.validDueMillis() >= currentTimeMillis
    }

    val todayOverdue = todayTasks.count { task ->
        !task.completed &&
                task.validDueMillis() < currentTimeMillis
    }

    val upcomingTasks = remember(tasks, today) {
        val lastUpcomingDate = today.plusDays(7)

        tasks
            .filter { task ->
                val taskDate = task.toLocalDate(zoneId)

                !task.completed &&
                        taskDate.isAfter(today) &&
                        !taskDate.isAfter(lastUpcomingDate)
            }
            .sortedBy { task ->
                task.validDueMillis()
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomBar(
                currentRoute = BottomNavItem.Calendar.route,
                onTasksClick = onTasksClick,
                onCalendarClick = {},
                onStatsClick = onStatsClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 16.dp,
                bottom = 26.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CalendarHeader()
            }

            item {
                CalendarMonthCard(
                    displayedMonth = displayedMonth,
                    selectedDate = selectedDate,
                    today = today,
                    taskDates = tasksByDate.keys,
                    onPreviousMonth = {
                        displayedMonth =
                            displayedMonth.minusMonths(1)
                    },
                    onNextMonth = {
                        displayedMonth =
                            displayedMonth.plusMonths(1)
                    },
                    onDateSelected = { date ->
                        selectedDate = date
                    }
                )
            }

            item {
                TodaySummaryCard(
                    total = todayTasks.size,
                    completed = todayCompleted,
                    pending = todayPending,
                    overdue = todayOverdue
                )
            }

            item {
                SectionHeader(
                    title = selectedDate.sectionDateTitle(today),
                    subtitle = if (selectedDateTasks.isEmpty()) {
                        "No tasks scheduled"
                    } else {
                        "${selectedDateTasks.size} ${
                            if (selectedDateTasks.size == 1) {
                                "task"
                            } else {
                                "tasks"
                            }
                        }"
                    }
                )
            }

            if (selectedDateTasks.isEmpty()) {
                item {
                    EmptySelectedDateCard(
                        selectedDate = selectedDate
                    )
                }
            } else {
                items(
                    items = selectedDateTasks,
                    key = { task ->
                        task.id
                    }
                ) { task ->
                    TimelineTaskCard(
                        task = task,
                        currentTimeMillis = currentTimeMillis,
                        onClick = {
                            onTaskClick(task.id)
                        }
                    )
                }
            }

            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            item {
                SectionHeader(
                    title = "Upcoming",
                    subtitle = "Next 7 days"
                )
            }

            if (upcomingTasks.isEmpty()) {
                item {
                    EmptyUpcomingCard()
                }
            } else {
                items(
                    items = upcomingTasks,
                    key = { task ->
                        task.id
                    }
                ) { task ->
                    UpcomingTaskCard(
                        task = task,
                        today = today,
                        zoneId = zoneId,
                        onClick = {
                            onTaskClick(task.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Calendar",
                fontSize = 28.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "Plan and review your schedule",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
@Composable
private fun CalendarMonthCard(
    displayedMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    taskDates: Set<LocalDate>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 14.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPreviousMonth
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronLeft,
                        contentDescription = "Previous month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = displayedMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ) + " ${displayedMonth.year}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = onNextMonth
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "Next month",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            CalendarWeekHeader()

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            CalendarDaysGrid(
                displayedMonth = displayedMonth,
                selectedDate = selectedDate,
                today = today,
                taskDates = taskDates,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
private fun CalendarWeekHeader() {
    val weekdayLabels = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        weekdayLabels.forEach { weekday ->
            Text(
                text = weekday,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CalendarDaysGrid(
    displayedMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    taskDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = displayedMonth.atDay(1)
    val emptyCellsBeforeMonth =
        firstDayOfMonth.dayOfWeek.value - 1

    val calendarCells = buildList<LocalDate?> {
        repeat(emptyCellsBeforeMonth) {
            add(null)
        }

        for (day in 1..displayedMonth.lengthOfMonth()) {
            add(displayedMonth.atDay(day))
        }

        while (size % 7 != 0) {
            add(null)
        }
    }

    calendarCells
        .chunked(7)
        .forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                week.forEach { date ->
                    CalendarDayCell(
                        date = date,
                        selected = date == selectedDate,
                        isToday = date == today,
                        hasTasks = date != null &&
                                taskDates.contains(date),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (date != null) {
                                onDateSelected(date)
                            }
                        }
                    )
                }
            }
        }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate?,
    selected: Boolean,
    isToday: Boolean,
    hasTasks: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clickable(
                enabled = date != null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = when {
                                selected -> MaterialTheme.colorScheme.primary
                                isToday -> MaterialTheme.colorScheme.primaryContainer
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 13.sp,
                        fontWeight = if (
                            selected || isToday
                        ) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        },
                        color = if (selected) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            color = when {
                                !hasTasks -> Color.Transparent
                                selected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.primary
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun TodaySummaryCard(
    total: Int,
    completed: Int,
    pending: Int,
    overdue: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Today's Summary",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryItem(
                    modifier = Modifier.weight(1f),
                    value = total,
                    label = "Total",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    valueColor = MaterialTheme.colorScheme.primary
                )

                SummaryItem(
                    modifier = Modifier.weight(1f),
                    value = completed,
                    label = "Done",
                    backgroundColor = CalendarGreenBackground,
                    valueColor = CalendarGreen
                )

                SummaryItem(
                    modifier = Modifier.weight(1f),
                    value = pending,
                    label = "Pending",
                    backgroundColor = CalendarOrangeBackground,
                    valueColor = CalendarOrange
                )

                SummaryItem(
                    modifier = Modifier.weight(1f),
                    value = overdue,
                    label = "Overdue",
                    backgroundColor = CalendarRedBackground,
                    valueColor = CalendarRed
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    value: Int,
    label: String,
    backgroundColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(13.dp)
            )
            .padding(
                horizontal = 5.dp,
                vertical = 11.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TimelineTaskCard(
    task: Task,
    currentTimeMillis: Long,
    onClick: () -> Unit
) {
    val priorityColor = task.priority.priorityColor()

    val isOverdue = !task.completed &&
            task.validDueMillis() < currentTimeMillis

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = task.dueTime.ifBlank {
                        "Any time"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOverdue) {
                        CalendarRed
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = priorityColor,
                            shape = CircleShape
                        )
                )
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(58.dp)
                    .background(
                        color = priorityColor.copy(
                            alpha = 0.30f
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (task.completed) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (task.completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    maxLines = 2
                )

                if (task.description.isNotBlank()) {
                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = task.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TaskStatusIndicator(
                        completed = task.completed,
                        overdue = isOverdue
                    )

                    if (task.reminderEnabled) {
                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Icon(
                            imageVector = Icons.Outlined.Alarm,
                            contentDescription = "Reminder enabled",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = task.reminderText(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskStatusIndicator(
    completed: Boolean,
    overdue: Boolean
) {
    val icon = when {
        completed -> Icons.Outlined.CheckCircle
        overdue -> Icons.Outlined.WarningAmber
        else -> Icons.Outlined.RadioButtonUnchecked
    }

    val text = when {
        completed -> "Completed"
        overdue -> "Overdue"
        else -> "Pending"
    }

    val color = when {
        completed -> CalendarGreen
        overdue -> CalendarRed
        else -> CalendarOrange
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )

        Spacer(
            modifier = Modifier.width(5.dp)
        )

        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun UpcomingTaskCard(
    task: Task,
    today: LocalDate,
    zoneId: ZoneId,
    onClick: () -> Unit
) {
    val taskDate = task.toLocalDate(zoneId)
    val priorityColor = task.priority.priorityColor()

    val dateLabel = when (taskDate) {
        today.plusDays(1) -> "Tomorrow"
        else -> taskDate.format(
            DateTimeFormatter.ofPattern(
                "EEE, dd MMM",
                Locale.getDefault()
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(
                        color = priorityColor,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dateLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = task.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )

                    Text(
                        text = task.dueTime.ifBlank {
                            "Any time"
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (task.reminderEnabled) {
                        Spacer(
                            modifier = Modifier.width(9.dp)
                        )

                        Icon(
                            imageVector = Icons.Outlined.Alarm,
                            contentDescription = "Reminder enabled",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Text(
                text = task.priority.priorityLabel(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = priorityColor
            )
        }
    }
}

@Composable
private fun EmptySelectedDateCard(
    selectedDate: LocalDate
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 22.dp,
                    vertical = 25.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "No tasks for this date",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = selectedDate.format(
                    DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy",
                        Locale.getDefault()
                    )
                ),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EmptyUpcomingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your week is clear",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Upcoming tasks will appear here.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun Task.validDueMillis(): Long {
    return if (dueDateTimeMillis > 0L) {
        dueDateTimeMillis
    } else {
        dueDateMillis
    }
}

private fun Task.toLocalDate(
    zoneId: ZoneId
): LocalDate {
    return Instant
        .ofEpochMilli(validDueMillis())
        .atZone(zoneId)
        .toLocalDate()
}

private fun Task.reminderText(): String {
    return when (reminderMinutesBefore) {
        0 -> "At due time"
        10 -> "10 min before"
        30 -> "30 min before"
        60 -> "1 hour before"
        1440 -> "1 day before"
        else -> "$reminderMinutesBefore min before"
    }
}

private fun Priority.priorityColor(): Color {
    return when (this) {
        Priority.HIGH -> Color(0xFFEF4444)
        Priority.MEDIUM -> Color(0xFFF59E0B)
        Priority.LOW -> Color(0xFF22C55E)
    }
}

private fun Priority.priorityLabel(): String {
    return when (this) {
        Priority.HIGH -> "High"
        Priority.MEDIUM -> "Medium"
        Priority.LOW -> "Low"
    }
}

private fun LocalDate.sectionDateTitle(
    today: LocalDate
): String {
    return when (this) {
        today -> "Today's Schedule"
        today.plusDays(1) -> "Tomorrow's Schedule"
        today.minusDays(1) -> "Yesterday's Schedule"
        else -> format(
            DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM",
                Locale.getDefault()
            )
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun CalendarScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        val zoneId = ZoneId.systemDefault()
        val today = LocalDate.now(zoneId)

        fun dateMillis(
            date: LocalDate,
            hour: Int,
            minute: Int
        ): Long {
            return date
                .atTime(hour, minute)
                .atZone(zoneId)
                .toInstant()
                .toEpochMilli()
        }

        CalendarScreen(
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Finish internship report",
                    description = "Complete the implementation chapter.",
                    dueDate = today.format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                    ),
                    dueDateMillis = dateMillis(today, 9, 0),
                    dueTime = "9:00 AM",
                    dueDateTimeMillis = dateMillis(today, 9, 0),
                    reminderEnabled = true,
                    reminderMinutesBefore = 30,
                    priority = Priority.HIGH,
                    completed = false
                ),
                Task(
                    id = 2,
                    title = "Team meeting",
                    description = "Discuss progress with the supervisor.",
                    dueDate = today.format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                    ),
                    dueDateMillis = dateMillis(today, 12, 30),
                    dueTime = "12:30 PM",
                    dueDateTimeMillis = dateMillis(today, 12, 30),
                    reminderEnabled = true,
                    reminderMinutesBefore = 10,
                    priority = Priority.MEDIUM,
                    completed = true
                ),
                Task(
                    id = 3,
                    title = "Prepare project presentation",
                    dueDate = today.plusDays(1).format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                    ),
                    dueDateMillis = dateMillis(
                        today.plusDays(1),
                        16,
                        0
                    ),
                    dueTime = "4:00 PM",
                    dueDateTimeMillis = dateMillis(
                        today.plusDays(1),
                        16,
                        0
                    ),
                    reminderEnabled = false,
                    reminderMinutesBefore = 30,
                    priority = Priority.LOW,
                    completed = false
                )
            )
        )
    }
}