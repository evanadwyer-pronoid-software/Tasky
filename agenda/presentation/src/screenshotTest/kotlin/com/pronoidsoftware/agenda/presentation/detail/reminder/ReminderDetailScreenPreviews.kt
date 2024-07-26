package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.agenda.presentation.detail.AgendaDetailScreen
import com.pronoidsoftware.agenda.presentation.detail.AgendaDetailState
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

private val selectedDate = LocalDate(2022, 3, 1)
private val selectedNotificationTime = LocalDateTime(2022, 7, 21, 8, 0)
private val today = LocalDate(2024, 7, 19)
private val fixedClock = TestClock(today.atStartOfDayIn(TimeZone.currentSystemDefault()))

@Preview
@Composable
private fun ReminderDetailScreenPreview_Read() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                type = AgendaItemType.REMINDER,
                state = AgendaDetailState(
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    atTime = selectedNotificationTime,
                    isEditing = false,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_Read_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                type = AgendaItemType.REMINDER,
                state = AgendaDetailState(
                    title = "Project X",
                    selectedDate = selectedDate,
                    atTime = selectedNotificationTime,
                    isEditing = false,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_Edit() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                type = AgendaItemType.REMINDER,
                state = AgendaDetailState(
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    atTime = selectedNotificationTime,
                    isEditing = true,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_Edit_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                type = AgendaItemType.REMINDER,
                state = AgendaDetailState(
                    title = "Project X",
                    selectedDate = selectedDate,
                    atTime = selectedNotificationTime,
                    isEditing = true,
                ),
                onAction = {},
            )
        }
    }
}
