@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDatePicker
import kotlinx.datetime.LocalDate

// @Composable
// fun AgendaDetailDatePicker(
//    selectedDate: LocalDate,
//    expanded: Boolean,
//    toggleExpanded: () -> Unit,
//    onSelectDate: (LocalDate) -> Unit,
//    modifier: Modifier = Modifier,
//    clock: Clock = Clock.System,
//    content: @Composable () -> Unit = {},
// ) {
//    val datePickerState = DatePickerState(
//        locale = Locale.getDefault(),
//        initialSelectedDateMillis = selectedDate.toMillis(),
//    )
//
//    content()
//    if (expanded) {
//        DatePickerDialog(
//            modifier = modifier,
//            onDismissRequest = toggleExpanded,
//            confirmButton = {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                ) {
//                    TextButton(
//                        onClick = {
//                            toggleExpanded()
//                            onSelectDate(
//                                today(clock = clock),
//                            )
//                        },
//                    ) {
//                        Text(
//                            text = stringResource(id = R.string.today),
//                        )
//                    }
//                    TextButton(
//                        onClick = toggleExpanded,
//                    ) {
//                        Text(
//                            text = stringResource(id = R.string.cancel),
//                        )
//                    }
//                    TextButton(
//                        onClick = {
//                            toggleExpanded()
//                            onSelectDate(
//                                // Date Picker operates in UTC
//                                // But we want to stay local to the user
//                                datePickerState.selectedDateMillis!!.toLocalDateFromUTC(),
//                            )
//                        },
//                    ) {
//                        Text(
//                            text = stringResource(id = R.string.confirm),
//                        )
//                    }
//                }
//            },
//        ) {
//            DatePicker(
//                state = datePickerState,
//                showModeToggle = false,
//            )
//        }
//    }
// }
//
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
//        AgendaDetailDatePicker(
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

@Composable
fun AgendaDetailDatePicker(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    TaskyDatePicker(
        selectedDate = selectedDate,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        onSelectDate = onSelectDate,
        modifier = modifier,
    ) {
        IconButton(
            enabled = enabled,
            onClick = toggleExpanded,
            modifier = Modifier.alpha(if (enabled) 100f else 0f),
        ) {
            Icon(
                imageVector = ForwardChevronIcon,
                contentDescription = stringResource(id = R.string.edit_date),
                tint = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailDatePickerPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(false)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        AgendaDetailDatePicker(
            selectedDate = today(),
            expanded = expanded,
            toggleExpanded = toggleExpanded,
            onSelectDate = { },
            enabled = true,
        )
    }
}
