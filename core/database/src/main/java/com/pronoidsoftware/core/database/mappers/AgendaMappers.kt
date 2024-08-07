package com.pronoidsoftware.core.database.mappers

import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.util.toLocalDateTime
import com.pronoidsoftware.core.domain.util.toMillis
import java.util.UUID

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toLocalDateTime(),
        notificationDateTime = notificationDateTime.toLocalDateTime(),
    )
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        description = description,
        startDateTime = startDateTime.toMillis(),
        notificationDateTime = notificationDateTime.toMillis(),
    )
}
