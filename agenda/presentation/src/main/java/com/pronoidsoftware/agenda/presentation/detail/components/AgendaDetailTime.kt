package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.ui.formatHours
import com.pronoidsoftware.core.presentation.ui.toRelativeDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun AgendaDetailTime(
    timeDescription: String,
    localDateTime: LocalDateTime,
    onSelectTime: (LocalTime) -> Unit,
    timePickerExpanded: Boolean,
    toggleTimePickerExpanded: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    datePickerExpanded: Boolean,
    toggleDatePickerExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    clock: Clock = Clock.System,
    editEnabled: Boolean = false,
) {
    val textStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 15.sp,
    )
    val contentColor = MaterialTheme.colorScheme.onBackground

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = timeDescription,
            style = textStyle,
        )
        Text(
            text = localDateTime.time.formatHours().asString(),
            style = textStyle,
            color = contentColor,
        )
        AgendaDetailTimePicker(
            selectedTime = localDateTime.time,
            onSelectTime = onSelectTime,
            expanded = timePickerExpanded,
            toggleExpanded = toggleTimePickerExpanded,
            enabled = editEnabled,
            contentColor = contentColor,
        )
        Text(
            text = localDateTime.date.toRelativeDate(clock = clock).asString(),
            style = textStyle,
            color = contentColor,
        )
        AgendaDetailDatePicker(
            selectedDate = localDateTime.date,
            onSelectDate = onSelectDate,
            expanded = datePickerExpanded,
            toggleExpanded = toggleDatePickerExpanded,
            enabled = editEnabled,
            contentColor = contentColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailTimePreview() {
    TaskyTheme {
        var timePickerExpanded by remember {
            mutableStateOf(false)
        }
        val toggleTimePickerExpanded = {
            timePickerExpanded = !timePickerExpanded
        }
        var datePickerExpanded by remember {
            mutableStateOf(false)
        }
        val toggleDatePickerExpanded = {
            datePickerExpanded = !datePickerExpanded
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AgendaDetailTime(
                timeDescription = "At",
                localDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                onSelectTime = { },
                timePickerExpanded = timePickerExpanded,
                toggleTimePickerExpanded = toggleTimePickerExpanded,
                onSelectDate = { },
                datePickerExpanded = datePickerExpanded,
                toggleDatePickerExpanded = toggleDatePickerExpanded,
            )
            AgendaDetailTime(
                timeDescription = "At",
                localDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                onSelectTime = { },
                timePickerExpanded = timePickerExpanded,
                toggleTimePickerExpanded = toggleTimePickerExpanded,
                onSelectDate = { },
                datePickerExpanded = datePickerExpanded,
                toggleDatePickerExpanded = toggleDatePickerExpanded,
                editEnabled = true,
            )
        }
    }
}
