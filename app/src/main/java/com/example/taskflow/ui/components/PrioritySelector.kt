package com.example.taskflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
import com.example.taskflow.ui.theme.TaskFlowTheme

@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PriorityOption(
            priority = Priority.LOW,
            selected = selectedPriority == Priority.LOW,
            onClick = { onPrioritySelected(Priority.LOW) },
            modifier = Modifier.weight(1f)
        )

        PriorityOption(
            priority = Priority.MEDIUM,
            selected = selectedPriority == Priority.MEDIUM,
            onClick = { onPrioritySelected(Priority.MEDIUM) },
            modifier = Modifier.weight(1f)
        )

        PriorityOption(
            priority = Priority.HIGH,
            selected = selectedPriority == Priority.HIGH,
            onClick = { onPrioritySelected(Priority.HIGH) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PriorityOption(
    priority: Priority,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = when (priority) {
        Priority.LOW -> Color(0xFF22A06B)
        Priority.MEDIUM -> Color(0xFFFFA000)
        Priority.HIGH -> Color(0xFFE53935)
    }

    val priorityText = when (priority) {
        Priority.LOW -> "Low"
        Priority.MEDIUM -> "Medium"
        Priority.HIGH -> "High"
    }

    Surface(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                priorityColor
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        color = if (selected) {
            priorityColor.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                modifier = Modifier.size(20.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = priorityColor,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )

            Text(
                text = priorityText,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) {
                    priorityColor
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrioritySelectorPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        PrioritySelector(
            selectedPriority = Priority.MEDIUM,
            onPrioritySelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
