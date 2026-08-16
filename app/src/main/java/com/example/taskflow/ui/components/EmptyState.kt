package com.example.taskflow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.R
import com.example.taskflow.model.FilterType
import com.example.taskflow.ui.theme.TaskFlowTheme
import androidx.compose.material3.MaterialTheme


@Composable
fun EmptyState(
    searchQuery: String,
    selectedFilter: FilterType,
    modifier: Modifier = Modifier
) {
    val title: String
    val message: String

    when {
        searchQuery.isNotBlank() -> {
            title = "No matching tasks"
            message = "Try searching with a different keyword."
        }

        selectedFilter == FilterType.ACTIVE -> {
            title = "No active tasks"
            message = "You're all caught up. Great job!"
        }

        selectedFilter == FilterType.COMPLETED -> {
            title = "No completed tasks"
            message = "Complete a task and it will appear here."
        }

        else -> {
            title = "No tasks yet!"
            message =
                "You haven't created any tasks.\n" +
                        "Tap the + button to add your\n" +
                        "first task."
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.empty_tasks
            ),
            contentDescription = "No tasks illustration",
            modifier = Modifier.size(255.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = title,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        Text(
            text = message,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun EmptyStatePreview() {
    TaskFlowTheme(
        dynamicColor = false
    ) {
        EmptyState(
            searchQuery = "",
            selectedFilter = FilterType.ALL
        )
    }
}