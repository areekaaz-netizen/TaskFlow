package com.example.taskflow.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TimePickerField(
    selectedTime: String,
    onTimeSelected: (
        formattedTime: String,
        hour: Int,
        minute: Int
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openTimePicker() {
        val calendar = Calendar.getInstance()

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                val formattedTime = SimpleDateFormat(
                    "h:mm a",
                    Locale.getDefault()
                ).format(selectedCalendar.time)

                onTimeSelected(
                    formattedTime,
                    hourOfDay,
                    minute
                )
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .clickable { openTimePicker() },
        enabled = false,
        singleLine = true,
        label = { Text("Due Time") },
        placeholder = { Text("Select due time") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = "Select due time"
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.primary
        )
    )
}
