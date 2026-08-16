package com.example.taskflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.FilterType
import com.example.taskflow.model.SortType
import androidx.compose.material3.MaterialTheme

@Composable
fun SortRow(
    selectedFilter: FilterType,
    selectedSort: SortType,
    onFilterSelected: (FilterType) -> Unit,
    onSortSelected: (SortType) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            FilterButton(
                text = "All",
                selected = selectedFilter == FilterType.ALL,
                modifier = Modifier.weight(1f),
                onClick = { onFilterSelected(FilterType.ALL) }
            )

            FilterButton(
                text = "Active",
                selected = selectedFilter == FilterType.ACTIVE,
                modifier = Modifier.weight(1f),
                onClick = { onFilterSelected(FilterType.ACTIVE) }
            )

            FilterButton(
                text = "Completed",
                selected = selectedFilter == FilterType.COMPLETED,
                modifier = Modifier.weight(1.35f),
                onClick = { onFilterSelected(FilterType.COMPLETED) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sort by:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(10.dp))

            Surface(
                modifier = Modifier.clickable {
                    expanded = true
                },
                color = Color.Transparent
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedSort.label(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Sort options",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    containerColor = MaterialTheme.colorScheme.surface
                )
                {
                    SortType.entries.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label(),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                expanded = false
                                onSortSelected(option)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        shadowElevation = if (selected) 3.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 9.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

private fun SortType.label(): String =
    when (this) {
        SortType.DUE_DATE -> "Due Date"
        SortType.PRIORITY -> "Priority"
        SortType.TITLE_A_TO_Z -> "Title A - Z"
        SortType.TITLE_Z_TO_A -> "Title Z - A"
    }
