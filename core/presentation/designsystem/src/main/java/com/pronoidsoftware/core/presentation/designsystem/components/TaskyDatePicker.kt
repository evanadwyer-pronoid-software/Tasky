@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskyDatePicker(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    clock: Clock = Clock.System,
    content: @Composable () -> Unit,
) {
    content()
    if (expanded) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toMillis(),
        )

        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = toggleExpanded,
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TextButton(
                        onClick = {
                            toggleExpanded()
                            onSelectDate(
                                today(clock = clock),
                            )
                        },
                    ) {
                        Text(text = stringResource(id = R.string.today))
                    }
                    TextButton(onClick = toggleExpanded) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            toggleExpanded()
                            onSelectDate(
                                datePickerState.selectedDateMillis?.toLocalDateFromUTC()
                                    ?: selectedDate,
                            )
                        },
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            },
        ) {
            DatePicker(state = datePickerState, showModeToggle = false)
        }
    }
}

private fun LocalDate.toMillis(): Long {
    return this.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

private fun Long.toLocalDateFromUTC(): LocalDate {
    return Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date
}
