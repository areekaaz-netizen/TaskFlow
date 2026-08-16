package com.example.taskflow.ui.components.statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.ui.theme.TaskFlowTheme

private val statisticsPeriods = listOf(
    "Today",
    "This Week",
    "This Month",
    "This Year"
)

@Composable
fun StatisticsDropdown(
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .width(145.dp)
                .height(42.dp)
                .clickable {
                    expanded = true
                },
            shape = RoundedCornerShape(11.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .height(42.dp)
                    .padding(
                        start = 13.dp,
                        end = 7.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected,
                    modifier = Modifier.weight(1f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Icon(
                    imageVector =
                        Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Select period",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.width(145.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            statisticsPeriods.forEach { period ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = period,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onSelected(period)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsDropdownPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        StatisticsDropdown(
            selected = "This Week",
            onSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}