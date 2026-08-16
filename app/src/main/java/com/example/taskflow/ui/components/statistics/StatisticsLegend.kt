package com.example.taskflow.ui.components.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.ui.theme.TaskFlowTheme

private val LegendCompleted = Color(0xFF4CAF50)
private val LegendActive = Color(0xFF5B5FEF)
private val LegendOverdue = Color(0xFFE53935)

@Composable
fun StatisticsLegend(
    completed: Int,
    active: Int,
    overdue: Int,
    modifier: Modifier = Modifier
) {
    val total = completed + active + overdue

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        LegendItem(
            color = LegendCompleted,
            title = "Completed",
            value = completed,
            percentage = calculatePercentage(
                value = completed,
                total = total
            )
        )

        LegendItem(
            color = LegendActive,
            title = "Active",
            value = active,
            percentage = calculatePercentage(
                value = active,
                total = total
            )
        )

        LegendItem(
            color = LegendOverdue,
            title = "Overdue",
            value = overdue,
            percentage = calculatePercentage(
                value = overdue,
                total = total
            )
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    title: String,
    value: Int,
    percentage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )

        Spacer(
            modifier = Modifier.width(9.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )

        Text(
            text = "$value ($percentage%)",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

private fun calculatePercentage(
    value: Int,
    total: Int
): Int {
    return if (total == 0) {
        0
    } else {
        ((value.toFloat() / total) * 100)
            .toInt()
    }
}

@Preview(
    showBackground = true,
    widthDp = 220
)
@Composable
private fun StatisticsLegendPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        StatisticsLegend(
            completed = 12,
            active = 6,
            overdue = 2,
            modifier = Modifier.padding(16.dp)
        )
    }
}