package com.example.taskflow.data.local

import androidx.room.TypeConverter
import com.example.taskflow.model.Priority

class PriorityConverters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority =
        runCatching { Priority.valueOf(value) }
            .getOrDefault(Priority.MEDIUM)
}
