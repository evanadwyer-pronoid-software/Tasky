package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Preview
@Composable
private fun ReminderDetailScreenPreview_Read() {
    TaskyTheme {
        ReminderDetailScreen(
            state = ReminderDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = false,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_Edit() {
    TaskyTheme {
        ReminderDetailScreen(
            state = ReminderDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
            ),
            onAction = {},
        )
    }
}
