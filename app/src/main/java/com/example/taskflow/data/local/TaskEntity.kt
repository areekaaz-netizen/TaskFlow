package com.example.taskflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskflow.model.Priority
import com.example.taskflow.model.Task

@Entity(
    tableName = "tasks"
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String,

    val dueDate: String,

    val dueDateMillis: Long,

    val dueTime: String,

    val dueDateTimeMillis: Long,

    val reminderEnabled: Boolean,

    val reminderMinutesBefore: Int,

    val priority: Priority,

    val completed: Boolean,

    val createdAtMillis: Long,

    val completedAtMillis: Long?
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        dueDateMillis = dueDateMillis,
        dueTime = dueTime,
        dueDateTimeMillis = dueDateTimeMillis,
        reminderEnabled = reminderEnabled,
        reminderMinutesBefore = reminderMinutesBefore,
        priority = priority,
        completed = completed,
        createdAtMillis = createdAtMillis,
        completedAtMillis = completedAtMillis
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        dueDateMillis = dueDateMillis,
        dueTime = dueTime,
        dueDateTimeMillis = dueDateTimeMillis,
        reminderEnabled = reminderEnabled,
        reminderMinutesBefore = reminderMinutesBefore,
        priority = priority,
        completed = completed,
        createdAtMillis = createdAtMillis,
        completedAtMillis = completedAtMillis
    )
}