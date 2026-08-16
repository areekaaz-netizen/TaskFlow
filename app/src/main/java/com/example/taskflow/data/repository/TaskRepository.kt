package com.example.taskflow.data.repository

import com.example.taskflow.data.local.TaskDao
import com.example.taskflow.data.local.toEntity
import com.example.taskflow.data.local.toTask
import com.example.taskflow.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(
    private val taskDao: TaskDao
) {
    val tasks: Flow<List<Task>> =
        taskDao
            .observeAllTasks()
            .map { entities ->
                entities.map { entity ->
                    entity.toTask()
                }
            }

    fun observeTaskById(
        taskId: Int
    ): Flow<Task?> {
        return taskDao
            .observeTaskById(taskId)
            .map { entity ->
                entity?.toTask()
            }
    }

    suspend fun insertTask(
        task: Task
    ): Long {
        return taskDao.insertTask(
            task.toEntity()
        )
    }

    suspend fun updateTask(
        task: Task
    ) {
        taskDao.updateTask(
            task.toEntity()
        )
    }

    suspend fun deleteTask(
        task: Task
    ) {
        taskDao.deleteTask(
            task.toEntity()
        )
    }

    suspend fun deleteTaskById(
        taskId: Int
    ) {
        taskDao.deleteTaskById(
            taskId
        )
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}