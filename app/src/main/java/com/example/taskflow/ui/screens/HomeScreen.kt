package com.example.taskflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.data.local.TaskDatabase
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.model.FilterType
import com.example.taskflow.model.SortType
import com.example.taskflow.model.Task
import com.example.taskflow.ui.components.BottomBar
import com.example.taskflow.ui.components.EmptyState
import com.example.taskflow.ui.components.SearchBar
import com.example.taskflow.ui.components.SortRow
import com.example.taskflow.ui.components.SummaryCard
import com.example.taskflow.ui.components.TaskCard
import com.example.taskflow.ui.components.TopBar
import com.example.taskflow.ui.theme.TaskFlowTheme
import com.example.taskflow.viewmodel.TaskUiState
import com.example.taskflow.viewmodel.TaskViewModel
import com.example.taskflow.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch
import com.example.taskflow.ui.navigation.BottomNavItem
import androidx.compose.material3.MaterialTheme

private val DeleteRed = Color(0xFFE53935)

@Composable
fun HomeScreen(
    onAddTaskClick: () -> Unit = {},
    onTaskClick: (Int) -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onStatisticsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    taskViewModel: TaskViewModel = provideTaskViewModel()
) {
    val uiState by taskViewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()

    HomeContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onSearchChange = taskViewModel::updateSearchQuery,
        onFilterChange = taskViewModel::updateFilter,
        onSortChange = taskViewModel::updateSort,
        onAddTaskClick = onAddTaskClick,
        onTaskClick = onTaskClick,
        onToggleTask = taskViewModel::toggleTask,
        onDeleteTask = { task ->
            taskViewModel.deleteTask(task)

            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Task deleted",
                    actionLabel = "Undo",
                    withDismissAction = true
                )

                if (result == SnackbarResult.ActionPerformed) {
                    taskViewModel.restoreTask(task)
                }
            }
        },
        onCalendarClick = onCalendarClick,
        onStatisticsClick = onStatisticsClick,
        onSettingsClick = onSettingsClick,
        onNotificationsClick = onNotificationsClick
    )
}
@Composable
private fun provideTaskViewModel(): TaskViewModel {
    val context = LocalContext.current.applicationContext

    val factory = remember(context) {
        val database = TaskDatabase.getInstance(context)

        val repository = TaskRepository(
            database.taskDao()
        )

        TaskViewModelFactory(repository)
    }

    return viewModel(
        factory = factory
    )
}
@Composable
private fun HomeContent(
    uiState: TaskUiState,
    snackbarHostState: SnackbarHostState,
    onSearchChange: (String) -> Unit,
    onFilterChange: (FilterType) -> Unit,
    onSortChange: (SortType) -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onToggleTask: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onCalendarClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val databaseIsEmpty = uiState.totalTasks == 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(58.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            BottomBar(
                currentRoute = BottomNavItem.Tasks.route,
                onTasksClick = {},
                onCalendarClick = onCalendarClick,
                onStatsClick = onStatisticsClick,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 14.dp,
                bottom = 82.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TopBar(
                    onNotificationClick = onNotificationsClick
                )
            }
            item {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchChange
                )
            }

            item {
                SortRow(
                    selectedFilter = uiState.selectedFilter,
                    selectedSort = uiState.selectedSort,
                    onFilterSelected = onFilterChange,
                    onSortSelected = onSortChange
                )
            }
            if (!databaseIsEmpty) {
                item {
                    SummaryCard(
                        completedTasks = uiState.completedTasks,
                        totalTasks = uiState.totalTasks
                    )
                }
            }
            if (uiState.tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = if (databaseIsEmpty) {
                                    390.dp
                                } else {
                                    300.dp
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState(
                            searchQuery = uiState.searchQuery,
                            selectedFilter = uiState.selectedFilter
                        )
                    }
                }
            } else {
                items(
                    items = uiState.tasks,
                    key = { task ->
                        task.id
                    }
                ) { task ->
                    SwipeDeleteTask(
                        task = task,
                        onTaskClick = {
                            onTaskClick(task.id)
                        },
                        onCheckedChange = { completed ->
                            onToggleTask(
                                task,
                                completed
                            )
                        },
                        onDelete = {
                            onDeleteTask(task)
                        }
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeDeleteTask(
    task: Task,
    onTaskClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            if (
                newValue ==
                SwipeToDismissBoxValue.EndToStart
            ) {
                onDelete()
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = DeleteRed,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = Color.White
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "Delete",
                        color = Color.White
                    )
                }
            }
        }
    ) {
        TaskCard(
            task = task,
            onTaskClick = onTaskClick,
            onCheckedChange = onCheckedChange
        )
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HomeScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        HomeContent(
            uiState = TaskUiState(
                tasks = emptyList(),
                totalTasks = 0,
                completedTasks = 0
            ),
            snackbarHostState = remember {
                SnackbarHostState()
            },
            onSearchChange = {},
            onFilterChange = {},
            onSortChange = {},
            onAddTaskClick = {},
            onTaskClick = {},
            onToggleTask = { _, _ -> },
            onDeleteTask = {},
            onCalendarClick = {},
            onStatisticsClick = {},
            onSettingsClick = {},
            onNotificationsClick = {}
        )
    }
}