package com.example.taskflow.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val initialCalendar = remember(selectedDate) {
        Calendar.getInstance().apply {
            if (selectedDate.isNotBlank()) {
                runCatching {
                    val formatter = SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )
                    formatter.parse(selectedDate)?.let { parsedDate ->
                        time = parsedDate
                    }
                }
            }
        }
    }
    val openDatePicker = {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val chosenDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val formattedDate = SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                ).format(chosenDate.time)

                onDateSelected(
                    formattedDate,
                    chosenDate.timeInMillis
                )
            },
            initialCalendar.get(Calendar.YEAR),
            initialCalendar.get(Calendar.MONTH),
            initialCalendar.get(Calendar.DAY_OF_MONTH)
        )
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .clickable { openDatePicker() },
        enabled = false,
        singleLine = true,
        label = { Text("Due Date") },
        placeholder = { Text("Select due date") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Select due date"
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
