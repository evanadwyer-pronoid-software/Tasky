package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.detail.AgendaDetailScreen
import com.pronoidsoftware.agenda.presentation.detail.AgendaDetailState
import com.pronoidsoftware.agenda.presentation.detail.AgendaItemDetails
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

private val selectedDate = LocalDate(2022, 3, 1)
private val startDateTime = LocalDateTime(2022, 7, 21, 8, 0)
private val endDateTime = LocalDateTime(2022, 7, 21, 8, 30)
private val today = LocalDate(2024, 7, 19)
private val fixedClock = TestClock(today.atStartOfDayIn(TimeZone.currentSystemDefault()))

private val photos = listOf(
    PhotoId.PhotoResId(R.drawable.test_wedding),
    PhotoId.PhotoResId(R.drawable.solid_orange),
)

private val visitors = listOf(
    VisitorUI(
        fullName = "Ann Allen",
        isCreator = true,
        isGoing = true,
    ),
    VisitorUI(
        fullName = "Wade Warren",
        isGoing = true,
    ),
    VisitorUI(
        fullName = "Esther Howard",
        isGoing = true,
    ),
    VisitorUI(
        fullName = "Jenny Wilson",
    ),
    VisitorUI(
        fullName = "Brooklyn Simmons",
    ),
)

@Preview
@Composable
private fun ReminderDetailScreenPreview_Read() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
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
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
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
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
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
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailScreenPreview_Read() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.TASK,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailScreenPreview_Read_Completed() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.TASK,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                    typeSpecificDetails = AgendaItemDetails.Task(
                        completed = true,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailScreenPreview_Read_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.TASK,
                    title = "Project X",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailScreenPreview_Edit() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.TASK,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun TaskDetailScreenPreview_Edit_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.TASK,
                    title = "Project X",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read_Empty() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    selectedDate = selectedDate,
                    agendaItemType = AgendaItemType.EVENT,
                    startDateTime = startDateTime,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        endDateTime = endDateTime,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read_OpenPhoto() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        selectedPhotoToView = PhotoId.PhotoResId(R.drawable.test_wedding),
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read_GoingVisitors() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                        selectedVisitorFilter = VisitorFilterType.GOING,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read_NotGoingVisitors() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                        selectedVisitorFilter = VisitorFilterType.NOT_GOING,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Read_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = false,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Edit() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Edit_AddVisitor() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                        isShowingAddVisitorDialog = true,
                    ),
                ),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailScreenPreview_Edit_EmptyDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.EVENT,
                    title = "Meeting",
                    selectedDate = selectedDate,
                    startDateTime = startDateTime,
                    isEditing = true,
                    typeSpecificDetails = AgendaItemDetails.Event(
                        photos = photos,
                        endDateTime = endDateTime,
                        visitors = visitors,
                    ),
                ),
                onAction = {},
            )
        }
    }
}
