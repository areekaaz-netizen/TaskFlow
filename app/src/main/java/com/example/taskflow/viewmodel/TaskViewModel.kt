package com.example.taskflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.model.FilterType
import com.example.taskflow.model.Priority
import com.example.taskflow.model.SortType
import com.example.taskflow.model.Task
import com.example.taskflow.model.TaskStatistics
import com.example.taskflow.notifications.ReminderScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TaskUiState(
    // Complete Room list, unaffected by Home filters.
    val allTasks: List<Task> = emptyList(),

    // List currently visible on Home.
    val tasks: List<Task> = emptyList(),

    val searchQuery: String = "",

    val selectedFilter: FilterType =
        FilterType.ALL,

    val selectedSort: SortType =
        SortType.DUE_DATE,

    val totalTasks: Int = 0,

    val completedTasks: Int = 0
)
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private var reminderScheduler: ReminderScheduler? = null

    fun attachReminderScheduler(scheduler: ReminderScheduler) {
        reminderScheduler = scheduler
    }

    private val searchQuery =
        MutableStateFlow("")

    private val selectedFilter =
        MutableStateFlow(
            FilterType.ALL
        )
    private val selectedSort =
        MutableStateFlow(
            SortType.DUE_DATE
        )

    val uiState: StateFlow<TaskUiState> =
        combine(
            repository.tasks,
            searchQuery,
            selectedFilter,
            selectedSort
        ) { allTasks, query, filter, sort ->

            val visibleTasks =
                allTasks
                    .filter { task ->
                        val matchesSearch =
                            task.title.contains(
                                other = query,
                                ignoreCase = true
                            ) ||
                                    task.description.contains(
                                        other = query,
                                        ignoreCase = true
                                    )

                        val matchesFilter =
                            when (filter) {
                                FilterType.ALL -> {
                                    true
                                }

                                FilterType.ACTIVE -> {
                                    !task.completed
                                }

                                FilterType.COMPLETED -> {
                                    task.completed
                                }
                            }

                        matchesSearch &&
                                matchesFilter
                    }
                    .let { matchingTasks ->
                        when (sort) {
                            SortType.DUE_DATE -> {
                                matchingTasks.sortedBy {
                                    it.dueDateMillis
                                }
                            }

                            SortType.PRIORITY -> {
                                matchingTasks
                                    .sortedByDescending {
                                        it.priority
                                            .sortRank()
                                    }
                            }

                            SortType.TITLE_A_TO_Z -> {
                                matchingTasks.sortedBy {
                                    it.title.lowercase()
                                }
                            }

                            SortType.TITLE_Z_TO_A -> {
                                matchingTasks
                                    .sortedByDescending {
                                        it.title.lowercase()
                                    }
                            }
                        }
                    }

            TaskUiState(
                allTasks = allTasks,
                tasks = visibleTasks,
                searchQuery = query,
                selectedFilter = filter,
                selectedSort = sort,
                totalTasks = allTasks.size,
                completedTasks =
                    allTasks.count {
                        it.completed
                    }
            )
        }.stateIn(
            scope = viewModelScope,
            started =
                SharingStarted
                    .WhileSubscribed(
                        stopTimeoutMillis = 5_000
                    ),
            initialValue = TaskUiState()
        )

    fun updateSearchQuery(
        query: String
    ) {
        searchQuery.value = query
    }

    fun updateFilter(
        filter: FilterType
    ) {
        selectedFilter.value = filter
    }

    fun updateSort(
        sort: SortType
    ) {
        selectedSort.value = sort
    }

    fun observeTaskById(
        taskId: Int
    ): Flow<Task?> {
        return repository.observeTaskById(
            taskId
        )
    }

    fun addTask(
        task: Task
    ) {
        viewModelScope.launch {
            val taskToInsert = task.copy(
                createdAtMillis = if (task.createdAtMillis > 0L) {
                    task.createdAtMillis
                } else {
                    System.currentTimeMillis()
                }
            )

            val insertedId = repository.insertTask(taskToInsert).toInt()
            val savedTask = taskToInsert.copy(id = insertedId)
            reminderScheduler?.scheduleReminder(savedTask)
        }
    }

    fun updateTask(
        task: Task
    ) {
        viewModelScope.launch {
            repository.updateTask(task)

            if (task.completed || !task.reminderEnabled) {
                reminderScheduler?.cancelReminder(task.id)
            } else {
                reminderScheduler?.scheduleReminder(task)
            }
        }
    }

    fun toggleTask(
        task: Task,
        completed: Boolean
    ) {
        val completionTime = when {
            completed && task.completedAtMillis == null -> System.currentTimeMillis()
            completed -> task.completedAtMillis
            else -> null
        }

        updateTask(
            task.copy(
                completed = completed,
                completedAtMillis = completionTime
            )
        )
    }

    fun deleteTask(
        task: Task
    ) {
        viewModelScope.launch {
            reminderScheduler?.cancelReminder(task.id)
            repository.deleteTask(task)
        }
    }

    fun restoreTask(
        task: Task
    ) {
        viewModelScope.launch {
            val insertedId = repository.insertTask(task).toInt()
            val restoredTask = if (task.id > 0) task else task.copy(id = insertedId)
            reminderScheduler?.scheduleReminder(restoredTask)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            reminderScheduler?.cancelAllReminders()
            repository.deleteAllTasks()
        }
    }

    fun getStatistics(): TaskStatistics {
        val tasks =
            uiState.value.allTasks

        val total = tasks.size

        val completed =
            tasks.count {
                it.completed
            }

        val pending =
            total - completed

        val high =
            tasks.count {
                it.priority ==
                        Priority.HIGH
            }

        val medium =
            tasks.count {
                it.priority ==
                        Priority.MEDIUM
            }

        val low =
            tasks.count {
                it.priority ==
                        Priority.LOW
            }

        val percentage =
            if (total == 0) {
                0f
            } else {
                completed * 100f /
                        total
            }

        return TaskStatistics(
            totalTasks = total,
            completedTasks = completed,
            pendingTasks = pending,
            highPriorityTasks = high,
            mediumPriorityTasks = medium,
            lowPriorityTasks = low,
            completionPercentage =
                percentage
        )
    }

    private fun Priority.sortRank(): Int {
        return when (this) {
            Priority.HIGH -> 3
            Priority.MEDIUM -> 2
            Priority.LOW -> 1
        }
    }
}