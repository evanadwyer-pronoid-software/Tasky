package com.pronoidsoftware.agenda.network.mappers

import com.pronoidsoftware.agenda.network.dto.ReminderDto
import com.pronoidsoftware.agenda.network.dto.TaskDto
import com.pronoidsoftware.agenda.network.request.UpsertReminderRequest
import com.pronoidsoftware.agenda.network.request.UpsertTaskRequest
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

fun AgendaItem.Reminder.toUpsertReminderRequest(): UpsertReminderRequest {
    return UpsertReminderRequest(
        id = id,
        title = title,
        description = description,
        time = startDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
    )
}

fun TaskDto.toTask(): AgendaItem.Task {
    return AgendaItem.Task(
        id = id,
        title = title,
        description = description,
        startDateTime = time.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
        isCompleted = isDone,
    )
}

fun AgendaItem.Task.toUpsertTaskRequest(): UpsertTaskRequest {
    return UpsertTaskRequest(
        id = id,
        title = title,
        description = description,
        time = startDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
        isDone = isCompleted,
    )
}
