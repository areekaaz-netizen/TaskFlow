package com.example.taskflow.ui.components.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.ui.theme.TaskFlowTheme

@Composable
fun ProgressLineChart(
    values: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    require(values.size == labels.size) {
        "Values and labels must have the same size."
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val titleColor = MaterialTheme.colorScheme.onBackground
    val pointCenterColor = MaterialTheme.colorScheme.surface

    val maximumValue = values.maxOrNull()
        ?.coerceAtLeast(1)
        ?: 1

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Progress",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(118.dp)
                .padding(
                    top = 16.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 6.dp
                )
        ) {
            if (values.isEmpty()) {
                return@Canvas
            }

            val horizontalSections = 3

            repeat(horizontalSections + 1) { index ->
                val y =
                    size.height * index /
                            horizontalSections.toFloat()

                drawLine(
                    color = gridColor,
                    start = Offset(
                        x = 0f,
                        y = y
                    ),
                    end = Offset(
                        x = size.width,
                        y = y
                    ),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val stepX = if (values.size == 1) {
                0f
            } else {
                size.width /
                        (values.size - 1).toFloat()
            }

            val points = values.mapIndexed { index, value ->
                val x = stepX * index

                val normalizedValue =
                    value.toFloat() /
                            maximumValue.toFloat()

                val y =
                    size.height -
                            (normalizedValue * size.height)

                Offset(
                    x = x,
                    y = y
                )
            }

            val linePath = Path().apply {
                moveTo(
                    points.first().x,
                    points.first().y
                )

                points.drop(1).forEach { point ->
                    lineTo(
                        point.x,
                        point.y
                    )
                }
            }

            drawPath(
                path = linePath,
                color = primaryColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            points.forEach { point ->
                drawCircle(
                    color = pointCenterColor,
                    radius = 5.dp.toPx(),
                    center = point
                )

                drawCircle(
                    color = primaryColor,
                    radius = 3.5.dp.toPx(),
                    center = point
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp,
                    color = labelColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 380
)
@Composable
private fun ProgressLineChartPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        ProgressLineChart(
            values = listOf(
                1,
                2,
                1,
                3,
                2,
                4,
                3
            ),
            labels = listOf(
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat",
                "Sun"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}