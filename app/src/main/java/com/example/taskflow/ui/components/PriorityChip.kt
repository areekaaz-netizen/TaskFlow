package com.example.taskflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
@Composable
fun PriorityChip(
    priority: Priority
) {

    val backgroundColor = when (priority) {
        Priority.HIGH -> Color(0xFFFFEBEE)
        Priority.MEDIUM -> Color(0xFFFFF3E0)
        Priority.LOW -> Color(0xFFE8F5E9)
    }

    val textColor = when (priority) {
        Priority.HIGH -> Color(0xFFE53935)
        Priority.MEDIUM -> Color(0xFFFB8C00)
        Priority.LOW -> Color(0xFF43A047)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50)
    ) {

        Text(
            text = priority.name.lowercase()
                .replaceFirstChar { it.uppercase() },
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = androidx.compose.ui.Modifier
                .background(Color.Transparent)
                .padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
                )
        )

    }

}