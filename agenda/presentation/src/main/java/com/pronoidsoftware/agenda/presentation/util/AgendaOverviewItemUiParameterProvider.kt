package com.pronoidsoftware.agenda.presentation.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi

internal class AgendaOverviewItemUiParameterProvider :
    PreviewParameterProvider<AgendaOverviewItemUi> {
    override val values: Sequence<AgendaOverviewItemUi> =
        sequenceOf(
            AgendaOverviewItemUi.TaskOverviewUi(
                id = "Task",
                title = "Project X",
                description = "Just work",
//                fromTime = LocalDateTime(2023, 3, 5, 10, 0),
                fromTime = "Mar 5, 10:00",
                completed = true,
            ),
            AgendaOverviewItemUi.EventOverviewUi(
                id = "Event",
                title = "Meeting",
                description = "Amet minim mollit non deserunt",
                fromTime = "Mar 5, 10:30",
                toTime = "Mar 5, 11:00",
//                fromTime = LocalDateTime(2023, 3, 5, 10, 30),
//                toTime = LocalDateTime(2023, 3, 5, 11, 0),
            ),
            AgendaOverviewItemUi.ReminderOverviewUi(
                id = "Reminder",
                title = "Lunch break",
                description = "Just work",
                fromTime = "Mar 5, 14:00",
//                fromTime = LocalDateTime(2023, 3, 5, 14, 0),
            ),
        )
}
