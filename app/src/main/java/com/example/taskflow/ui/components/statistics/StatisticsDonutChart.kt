package com.example.taskflow.ui.components.statistics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskflow.ui.theme.TaskFlowTheme

private val DonutCompleted = Color(0xFF4CAF50)
private val DonutActive = Color(0xFF5B5FEF)
private val DonutOverdue = Color(0xFFE53935)

@Composable
fun StatisticsDonutChart(
    completed: Int,
    active: Int,
    overdue: Int,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    val total = completed + active + overdue

    val completedAngle =
        if (total == 0) 0f
        else completed * 360f / total

    val activeAngle =
        if (total == 0) 0f
        else active * 360f / total

    val overdueAngle =
        if (total == 0) 0f
        else overdue * 360f / total

    val animation = remember {
        Animatable(0f)
    }

    LaunchedEffect(
        completed,
        active,
        overdue
    ) {
        animation.snapTo(0f)

        animation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 850
            )
        )
    }

    Canvas(
        modifier = modifier.size(128.dp)
    ) {
        val strokeWidth = 14.dp.toPx()

        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        var startAngle = -90f

        drawArc(
            color = DonutCompleted,
            startAngle = startAngle,
            sweepAngle =
                completedAngle * animation.value,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        startAngle += completedAngle

        drawArc(
            color = DonutActive,
            startAngle = startAngle,
            sweepAngle =
                activeAngle * animation.value,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        startAngle += activeAngle

        drawArc(
            color = DonutOverdue,
            startAngle = startAngle,
            sweepAngle =
                overdueAngle * animation.value,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsDonutChartPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        StatisticsDonutChart(
            completed = 12,
            active = 6,
            overdue = 2
        )
    }
}