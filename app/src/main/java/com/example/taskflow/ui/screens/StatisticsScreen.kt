package com.example.taskflow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
import com.example.taskflow.model.Task
import com.example.taskflow.model.TaskStatistics
import com.example.taskflow.ui.components.BottomBar
import com.example.taskflow.ui.components.statistics.ProgressLineChart
import com.example.taskflow.ui.components.statistics.StatisticsCard
import com.example.taskflow.ui.components.statistics.StatisticsDonutChart
import com.example.taskflow.ui.components.statistics.StatisticsDropdown
import com.example.taskflow.ui.components.statistics.StatisticsLegend
import com.example.taskflow.ui.navigation.BottomNavItem
import com.example.taskflow.ui.theme.TaskFlowTheme
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import androidx.compose.material3.MaterialTheme


private data class GraphData(
    val values: List<Int>,
    val labels: List<String>
)

@Composable
fun StatisticsScreen(
    statistics: TaskStatistics,
    overdueTasks: Int = 0,
    tasksForGraph: List<Task> = emptyList(),
    onTasksClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var selectedPeriod by remember {
        mutableStateOf("This Week")
    }

    /*
     * Overview stays exactly like before:
     * all tasks, completed, active and overdue.
     *
     * The dropdown only changes the graph.
     */
    val activeTasks = (
            statistics.totalTasks -
                    statistics.completedTasks -
                    overdueTasks
            ).coerceAtLeast(0)

    val graphData = remember(
        tasksForGraph,
        selectedPeriod
    ) {
        buildGraphData(
            tasks = tasksForGraph,
            period = selectedPeriod
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomBar(
                currentRoute = BottomNavItem.Statistics.route,
                onTasksClick = onTasksClick,
                onCalendarClick = onCalendarClick,
                onStatsClick = {},
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 12.dp,
                    bottom = 8.dp
                )
        ) {
            Text(
                text = "Statistics",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            StatisticsDropdown(
                selected = selectedPeriod,
                onSelected = { period ->
                    selectedPeriod = period
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Overview",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatisticsDonutChart(
                        completed = statistics.completedTasks,
                        active = activeTasks,
                        overdue = overdueTasks
                    )

                    Spacer(
                        modifier = Modifier.weight(0.12f)
                    )

                    StatisticsLegend(
                        completed = statistics.completedTasks,
                        active = activeTasks,
                        overdue = overdueTasks,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Summary",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatisticsCard(
                    title = "Total Tasks",
                    value = statistics.totalTasks,
                    modifier = Modifier.weight(1f)
                )

                StatisticsCard(
                    title = "Completed",
                    value = statistics.completedTasks,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatisticsCard(
                    title = "Active",
                    value = activeTasks,
                    modifier = Modifier.weight(1f)
                )

                StatisticsCard(
                    title = "Overdue",
                    value = overdueTasks,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            ProgressLineChart(
                values = graphData.values,
                labels = graphData.labels,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/*
 * The graph counts completed tasks using their due dates.
 *
 * This works with the tasks you already created and does not require
 * completion timestamps or another Room migration.
 */
private fun buildGraphData(
    tasks: List<Task>,
    period: String
): GraphData {
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)

    return when (period) {
        "Today" -> {
            buildTodayGraph(
                tasks = tasks,
                today = today,
                zone = zone
            )
        }

        "This Month" -> {
            buildMonthGraph(
                tasks = tasks,
                today = today,
                zone = zone
            )
        }

        "This Year" -> {
            buildYearGraph(
                tasks = tasks,
                today = today,
                zone = zone
            )
        }

        else -> {
            buildWeekGraph(
                tasks = tasks,
                today = today,
                zone = zone
            )
        }
    }
}

private fun buildTodayGraph(
    tasks: List<Task>,
    today: LocalDate,
    zone: ZoneId
): GraphData {
    val labels = listOf(
        "12a",
        "4a",
        "8a",
        "12p",
        "4p",
        "8p"
    )

    val values = MutableList(6) {
        0
    }

    /*
     * Due dates are stored at midnight, so tasks due today
     * appear in the first point.
     */
    tasks
        .filter { task ->
            task.completed &&
                    task.dueDateMillis
                        .toLocalDate(zone) == today
        }
        .forEach {
            values[0]++
        }

    return GraphData(
        values = values,
        labels = labels
    )
}

private fun buildWeekGraph(
    tasks: List<Task>,
    today: LocalDate,
    zone: ZoneId
): GraphData {
    val labels = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    val values = MutableList(7) {
        0
    }

    val weekStart = today.with(
        TemporalAdjusters.previousOrSame(
            DayOfWeek.MONDAY
        )
    )

    tasks
        .filter { task ->
            task.completed
        }
        .forEach { task ->
            val dueDate = task.dueDateMillis
                .toLocalDate(zone)

            val index = (
                    dueDate.toEpochDay() -
                            weekStart.toEpochDay()
                    ).toInt()

            if (index in 0..6) {
                values[index]++
            }
        }

    return GraphData(
        values = values,
        labels = labels
    )
}

private fun buildMonthGraph(
    tasks: List<Task>,
    today: LocalDate,
    zone: ZoneId
): GraphData {
    val labels = listOf(
        "W1",
        "W2",
        "W3",
        "W4",
        "W5"
    )

    val values = MutableList(5) {
        0
    }

    val currentMonth = YearMonth.from(
        today
    )

    tasks
        .filter { task ->
            task.completed
        }
        .forEach { task ->
            val dueDate = task.dueDateMillis
                .toLocalDate(zone)

            if (YearMonth.from(dueDate) == currentMonth) {
                val index = (
                        (dueDate.dayOfMonth - 1) / 7
                        ).coerceIn(0, 4)

                values[index]++
            }
        }

    return GraphData(
        values = values,
        labels = labels
    )
}

private fun buildYearGraph(
    tasks: List<Task>,
    today: LocalDate,
    zone: ZoneId
): GraphData {
    val labels = (1..12).map { monthNumber ->
        YearMonth
            .of(
                today.year,
                monthNumber
            )
            .month
            .getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            )
    }

    val values = MutableList(12) {
        0
    }

    tasks
        .filter { task ->
            task.completed
        }
        .forEach { task ->
            val dueDate = task.dueDateMillis
                .toLocalDate(zone)

            if (dueDate.year == today.year) {
                values[
                    dueDate.monthValue - 1
                ]++
            }
        }

    return GraphData(
        values = values,
        labels = labels
    )
}

private fun Long.toLocalDate(
    zone: ZoneId
): LocalDate {
    return Instant
        .ofEpochMilli(this)
        .atZone(zone)
        .toLocalDate()
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun StatisticsScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        val now = System.currentTimeMillis()

        val previewTasks = listOf(
            Task(
                id = 1,
                title = "Complete report",
                dueDate = "16 Jul 2026",
                dueDateMillis = now,
                priority = Priority.HIGH,
                completed = true
            ),
            Task(
                id = 2,
                title = "Prepare slides",
                dueDate = "17 Jul 2026",
                dueDateMillis =
                    now + 86_400_000L,
                priority = Priority.MEDIUM,
                completed = false
            ),
            Task(
                id = 3,
                title = "Review work",
                dueDate = "15 Jul 2026",
                dueDateMillis =
                    now - 86_400_000L,
                priority = Priority.LOW,
                completed = true
            )
        )

        StatisticsScreen(
            statistics = TaskStatistics(
                totalTasks = 3,
                completedTasks = 2,
                pendingTasks = 1,
                highPriorityTasks = 1,
                mediumPriorityTasks = 1,
                lowPriorityTasks = 1,
                completionPercentage = 66.7f
            ),
            overdueTasks = 0,
            tasksForGraph = previewTasks
        )
    }
}