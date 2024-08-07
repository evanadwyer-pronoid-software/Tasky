package com.pronoidsoftware.agenda.network.mappers

import com.pronoidsoftware.agenda.network.PostReminderRequest
import com.pronoidsoftware.agenda.network.dto.ReminderDto
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.util.toLocalDateTime
import com.pronoidsoftware.core.domain.util.toMillis

fun ReminderDto.toReminder(): AgendaItem.Reminder {
    return AgendaItem.Reminder(
        id = id,
        title = title,
        description = description,
        startDateTime = time.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
    )
}

fun AgendaItem.Reminder.toPostReminderRequest(): PostReminderRequest {
    return PostReminderRequest(
        id = id,
        title = title,
        description = description,
        time = startDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
    )
}
