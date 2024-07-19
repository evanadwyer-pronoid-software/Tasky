@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.presentation.designsystem.R
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime

@Composable
fun TaskyTimePicker(
    selectedTime: LocalTime,
    onSelectTime: (LocalTime) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    clock: Clock = Clock.System,
    content: @Composable () -> Unit,
) {
    content()
    if (expanded) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
        )

        // TimePickerDialog not shipped with Material3, reusing DatePickerDialog
        // https://issuetracker.google.com/issues/288311426
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
                            onSelectTime(
                                now(clock = clock).time,
                            )
                        },
                    ) {
                        Text(text = stringResource(id = R.string.now))
                    }
                    TextButton(onClick = toggleExpanded) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            toggleExpanded()
                            onSelectTime(LocalTime(timePickerState.hour, timePickerState.minute))
                        },
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Center,
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors().copy(
                        clockDialColor = TaskyLightGray,
                        containerColor = TaskyWhite,
                        periodSelectorSelectedContainerColor = TaskyGreen,
                        timeSelectorUnselectedContainerColor = TaskyLightGray,
                        timeSelectorSelectedContainerColor = TaskyGreen,
                        periodSelectorSelectedContentColor = TaskyWhite,
                        periodSelectorUnselectedContentColor = TaskyBlack,
                        periodSelectorUnselectedContainerColor = TaskyLightGray,
                    ),
                )
            }
        }
    }
}
