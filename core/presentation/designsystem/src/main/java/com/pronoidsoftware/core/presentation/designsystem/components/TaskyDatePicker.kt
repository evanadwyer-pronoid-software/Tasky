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
import com.pronoidsoftware.core.domain.util.toLocalDateFromUTC
import com.pronoidsoftware.core.domain.util.toMillis
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.R
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

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
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.toMillis(),
    )
    // set to support cancelling
    datePickerState.selectedDateMillis = selectedDate.toMillis()

    content()
    if (expanded) {
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
                                datePickerState.selectedDateMillis!!.toLocalDateFromUTC(),
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
