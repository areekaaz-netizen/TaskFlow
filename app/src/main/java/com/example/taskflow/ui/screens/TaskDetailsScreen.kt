package com.example.taskflow.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.model.Priority
import com.example.taskflow.model.Task
import com.example.taskflow.ui.theme.TaskFlowTheme
import androidx.compose.material.icons.outlined.Schedule

private val DetailsRed = Color(0xFFDC2626)
private val DetailsGreen = Color(0xFF16A34A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    task: Task?,
    onBackClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    onDeleteClick: (Task) -> Unit = {}
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // More actions can be added later.
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->

        if (task == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading task...",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 18.dp,
                    vertical = 8.dp
                )
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TaskDetailsHeader(
                        task = task
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DetailsSection(
                        title = "Description"
                    ) {
                        Text(
                            text = task.description.ifBlank {
                                "No description added."
                            },
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = if (task.description.isBlank()) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DetailsSection(
                        title = "Due Date"
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(19.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(9.dp)
                            )

                            Text(
                                text = task.dueDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    DetailsSection(
                        title = "Due Time"
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(19.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(9.dp)
                            )

                            Text(
                                text = task.dueTime.ifBlank {
                                    "No time selected"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (task.dueTime.isBlank()) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    DetailsSection(
                        title = "Status"
                    ) {
                        StatusChip(
                            completed = task.completed
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DetailsActionButton(
                            modifier = Modifier.weight(1f),
                            title = "Edit",
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            textColor = MaterialTheme.colorScheme.onSurface,
                            onClick = {
                                onEditClick(task.id)
                            }
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(26.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        DetailsActionButton(
                            modifier = Modifier.weight(1f),
                            title = "Delete",
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = DetailsRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            textColor = DetailsRed,
                            onClick = {
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog && task != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(
                    text = "Delete task?",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "This task will be permanently deleted.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(task)
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = DetailsRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun TaskDetailsHeader(
    task: Task
) {
    val priorityColor = task.priority.displayColor()
    val priorityText = task.priority.displayText()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 18.dp,
                top = 18.dp,
                bottom = 18.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(68.dp)
                .background(
                    color = priorityColor,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = if (task.completed) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = priorityText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = priorityColor
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (task.completed) {
                        DetailsGreen
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    shape = CircleShape
                )
                .then(
                    Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (task.completed) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun DetailsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 15.dp
            )
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        content()
    }
}

@Composable
private fun StatusChip(
    completed: Boolean
) {
    val containerColor = if (completed) {
        Color(0xFFE3F5E7)
    } else {
        Color(0xFFFFF1D8)
    }

    val textColor = if (completed) {
        DetailsGreen
    } else {
        Color(0xFFF59E0B)
    }

    Box(
        modifier = Modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 7.dp
            )
    ) {
        Text(
            text = if (completed) {
                "Completed"
            } else {
                "Pending"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
private fun DetailsActionButton(
    title: String,
    icon: @Composable () -> Unit,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(
            modifier = Modifier.width(9.dp)
        )

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

private fun Priority.displayColor(): Color {
    return when (this) {
        Priority.HIGH -> Color(0xFFEF4444)
        Priority.MEDIUM -> Color(0xFFF59E0B)
        Priority.LOW -> Color(0xFF22C55E)
    }
}

private fun Priority.displayText(): String {
    return when (this) {
        Priority.HIGH -> "High Priority"
        Priority.MEDIUM -> "Medium Priority"
        Priority.LOW -> "Low Priority"
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun TaskDetailsScreenPreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        TaskDetailsScreen(
            task = Task(
                id = 1,
                title = "Finish Android Assignment",
                description =
                    "Implement Room Database and connect it with the user interface.",
                dueDate = "15 Jul 2026",
                dueDateMillis = 1784073600000L,
                dueTime = "4:30 PM",
                dueDateTimeMillis = 1784118600000L,
                priority = Priority.HIGH,
                completed = true
            )
        )
    }
}