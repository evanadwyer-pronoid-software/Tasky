@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pronoidsoftware.core.domain.util.toLocalDateFromUTC
import com.pronoidsoftware.core.domain.util.toMillis
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.R
import java.util.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Composable
fun TaskyDatePicker(
    selectedDate: LocalDate,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    clock: Clock = Clock.System,
    content: @Composable () -> Unit,
) {
    val datePickerState = DatePickerState(
        locale = Locale.getDefault(),
        initialSelectedDateMillis = selectedDate.toMillis(),
    )

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
                        Text(
                            text = stringResource(id = R.string.today),
                        )
                    }
                    TextButton(
                        onClick = toggleExpanded,
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                        )
                    }
                    TextButton(
                        onClick = {
                            toggleExpanded()
                            onSelectDate(
                                // Date Picker operates in UTC
                                // But we want to stay local to the user
                                datePickerState.selectedDateMillis!!.toLocalDateFromUTC(),
                            )
                        },
                    ) {
                        Text(
                            text = stringResource(id = R.string.confirm),
                        )
                    }
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
            )
        }
    }
}

// @Preview
// @Composable
// private fun AgendaDetailDatePickerPreview() {
//    TaskyTheme {
//        var datePickerExpanded by remember {
//            mutableStateOf(false)
//        }
//        val toggleDatePickerExpanded = {
//            datePickerExpanded = !datePickerExpanded
//        }
//        TaskyDatePicker(
//            selectedDate = today(),
//            expanded = datePickerExpanded,
//            toggleExpanded = toggleDatePickerExpanded,
//            onSelectDate = { },
//        ) {
//            IconButton(onClick = toggleDatePickerExpanded) {
//                Icon(imageVector = ForwardChevronIcon, contentDescription = null)
//            }
//        }
//    }
// }
