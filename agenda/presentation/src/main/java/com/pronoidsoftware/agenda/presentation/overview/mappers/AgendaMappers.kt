package com.pronoidsoftware.agenda.presentation.overview.mappers

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemContents
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.presentation.ui.formatOverview

fun AgendaItem.Reminder.toReminderOverviewUi(): AgendaOverviewItemUi {
    return AgendaOverviewItemUi.Item(
        item = AgendaOverviewItemContents.ReminderOverviewUiContents(
            id = id,
            title = title,
            description = description ?: "",
            startDateTime = startDateTime.formatOverview(),
        ),
    )
}

fun AgendaItem.Task.toTaskOverviewUi(): AgendaOverviewItemUi {
    return AgendaOverviewItemUi.Item(
        item = AgendaOverviewItemContents.TaskOverviewUiContents(
            id = id,
            title = title,
            description = description ?: "",
            startDateTime = startDateTime.formatOverview(),
            completed = isCompleted,
        ),
    )
}

fun AgendaItem.Event.toEventOverviewUi(): AgendaOverviewItemUi {
    return AgendaOverviewItemUi.Item(
        item = AgendaOverviewItemContents.EventOverviewUiContents(
            id = id,
            title = title,
            description = description ?: "",
            startDateTime = startDateTime.formatOverview(),
            endDateTime = endDateTime.formatOverview(),
        ),
    )
}
