package com.pronoidsoftware.agenda.presentation.overview.mappers

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemContents
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.presentation.ui.formatOverview

fun Reminder.toReminderUi(): AgendaOverviewItemUi {
    return AgendaOverviewItemUi.Item(
        item = AgendaOverviewItemContents.ReminderOverviewUiContents(
            id = id!!,
            title = title,
            description = description ?: "",
            startDateTime = startDateTime.formatOverview(),
        ),
    )
}
