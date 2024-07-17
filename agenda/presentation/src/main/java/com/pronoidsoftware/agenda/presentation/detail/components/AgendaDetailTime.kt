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
import com.pronoidsoftware.core.presentation.ui.formatForHours
import com.pronoidsoftware.core.presentation.ui.toRelativeDateTwoYear
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun AgendaDetailTime(
    timeDesignation: String,
//    time: String,
//    date: String,
    onSelectDate: (LocalDate) -> Unit,
    localDateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
//    onEditTime: () -> Unit,
    onSelectTime: (LocalTime) -> Unit,
//    clock: Clock = Clock.System,
//    onEditDate: () -> Unit,
) {
    var datePickerExpanded by remember {
        mutableStateOf(false)
    }
    val toggleDatePickerExpanded = {
        datePickerExpanded = !datePickerExpanded
    }
    var timePickerExpanded by remember {
        mutableStateOf(false)
    }
    val toggleTimePickerExpanded = {
        timePickerExpanded = !timePickerExpanded
    }

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
            text = timeDesignation,
            style = textStyle,
            color = contentColor,
        )
        Text(
            text = localDateTime.time.formatForHours().asString(),
            style = textStyle,
            color = contentColor,
        )
//        IconButton(
//            enabled = isEditable,
//            onClick = onEditTime,
//            modifier = Modifier.alpha(if (isEditable) 1f else 0f),
//        ) {
//            Icon(
//                imageVector = ForwardChevronIcon,
//                contentDescription = stringResource(id = R.string.edit),
//                tint = contentColor,
//            )
//        }
        AgendaDetailTimePicker(
            selectedTime = localDateTime.time,
            onSelectTime = onSelectTime,
            expanded = timePickerExpanded,
            toggleExpanded = toggleTimePickerExpanded,
            enabled = isEditable,
            contentColor = contentColor,
        )
        Text(
            text = localDateTime.date.toRelativeDateTwoYear().asString(),
            style = textStyle,
            color = contentColor,
        )
        AgendaDetailDatePicker(
            selectedDate = localDateTime.date,
            expanded = datePickerExpanded,
            toggleExpanded = toggleDatePickerExpanded,
            onSelectDate = onSelectDate,
            enabled = isEditable,
            contentColor = contentColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailTimePreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AgendaDetailTime(
                timeDesignation = "At",
//                time = "08:00",
//                date = "July 21 2022",
                localDateTime = LocalDateTime(2022, 7, 21, 8, 0),
//                onEditTime = {},
                onSelectTime = {},
//                onEditDate = {},
                onSelectDate = {},
            )
            AgendaDetailTime(
                timeDesignation = "At",
//                time = "08:00",
//                date = "July 21 2022",
                localDateTime = LocalDateTime(2022, 7, 21, 8, 0),
//                onEditTime = {},
                onSelectTime = {},
//                onEditDate = {},
                onSelectDate = {},
                isEditable = true,
            )
        }
    }
}
