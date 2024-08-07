package com.pronoidsoftware.core.database.mappers

import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.database.entity.TaskEntity
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.util.toLocalDateTime
import com.pronoidsoftware.core.domain.util.toMillis
import java.util.UUID

fun ReminderEntity.toReminder(): AgendaItem.Reminder {
    return AgendaItem.Reminder(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toLocalDateTime(),
        notificationDateTime = notificationDateTime.toLocalDateTime(),
    )
}

fun AgendaItem.Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        description = description,
        startDateTime = startDateTime.toMillis(),
        notificationDateTime = notificationDateTime.toMillis(),
    )
}

fun TaskEntity.toTask(): AgendaItem.Task {
    return AgendaItem.Task(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toLocalDateTime(),
        notificationDateTime = notificationDateTime.toLocalDateTime(),
        isCompleted = isCompleted,
    )
}

fun AgendaItem.Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        description = description,
        startDateTime = startDateTime.toMillis(),
        notificationDateTime = notificationDateTime.toMillis(),
        isCompleted = isCompleted,
    )
}
