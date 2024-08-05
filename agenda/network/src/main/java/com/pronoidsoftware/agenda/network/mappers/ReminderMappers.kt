package com.pronoidsoftware.agenda.network.mappers

import com.pronoidsoftware.agenda.network.PostReminderRequest
import com.pronoidsoftware.agenda.network.ReminderDto
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.util.toLocalDateTime
import com.pronoidsoftware.core.domain.util.toMillis

fun ReminderDto.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        startDateTime = time.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
    )
}

fun Reminder.toPostReminderRequest(): PostReminderRequest {
    return PostReminderRequest(
        id = id!!,
        title = title,
        description = description,
        time = startDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
    )
}
