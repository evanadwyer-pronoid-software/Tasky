@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.ArrowDownIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDatePicker
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Composable
fun AgendaOverviewDatePicker(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    clock: Clock = Clock.System,
) {
    TaskyDatePicker(
        selectedDate = selectedDate,
        expanded = expanded,
        toggleExpanded = toggleExpanded,
        onSelectDate = onSelectDate,
        clock = clock,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clickable {
                    toggleExpanded()
                },
        ) {
            Text(
                text = selectedDate.month.name,
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor,
            )
            Icon(
                imageVector = ArrowDownIcon,
                tint = contentColor,
                contentDescription = if (expanded) {
                    null
                } else {
                    stringResource(id = R.string.select_date)
                },
            )
        }
    }
}

@Preview
@Composable
private fun AgendaOverviewDatePickerPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(false)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        AgendaOverviewDatePicker(
            selectedDate = today(),
            onSelectDate = { },
            expanded = expanded,
            toggleExpanded = toggleExpanded,
        )
    }
}

// @Composable
// fun AgendaOverviewDatePicker(
//    selectedDate: LocalDate,
//    onSelectDate: (LocalDate) -> Unit,
//    modifier: Modifier = Modifier,
//    clock: Clock = Clock.System,
// ) {
//    var openDialog by remember {
//        mutableStateOf(false)
//    }
//
//    // don't use rememberDatePickerState because we want to be able to set the selected date
//    // from the date widget on the agenda overview screen too
//    val datePickerState = DatePickerState(
//        locale = Locale.getDefault(),
//        initialSelectedDateMillis = selectedDate.toMillis(),
//    )
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .clickable {
//                openDialog = true
//            },
//    ) {
//        Text(
//            text = selectedDate.month.name,
//            style = MaterialTheme.typography.headlineMedium,
//            color = MaterialTheme.colorScheme.onPrimaryContainer,
//        )
//        Icon(
//            imageVector = ArrowDownIcon,
//            tint = MaterialTheme.colorScheme.onPrimaryContainer,
//            contentDescription = if (openDialog) {
//                null
//            } else {
//                stringResource(id = R.string.select_date)
//            },
//        )
//    }
//    if (openDialog) {
//        DatePickerDialog(
//            onDismissRequest = {
//                openDialog = false
//            },
//            confirmButton = {
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                ) {
//                    TextButton(
//                        onClick = {
//                            openDialog = false
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
//                        onClick = {
//                            openDialog = false
//                        },
//                    ) {
//                        Text(
//                            text = stringResource(id = R.string.cancel),
//                        )
//                    }
//                    TextButton(
//                        onClick = {
//                            openDialog = false
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
// private fun AgendaOverviewDatePickerPreview() {
//    TaskyTheme {
//        AgendaOverviewDatePicker(
//            selectedDate = today(),
//            onSelectDate = { },
//        )
//    }
// }
