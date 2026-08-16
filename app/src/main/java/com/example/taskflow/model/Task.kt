package com.example.taskflow.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",

    val dueDate: String,
    val dueDateMillis: Long,

    val dueTime: String = "",
    val dueDateTimeMillis: Long = dueDateMillis,

    val reminderEnabled: Boolean = false,
    val reminderMinutesBefore: Int = 30,

    val priority: Priority,
    val completed: Boolean = false,

    val createdAtMillis: Long =
        System.currentTimeMillis(),

    val completedAtMillis: Long? = null
)