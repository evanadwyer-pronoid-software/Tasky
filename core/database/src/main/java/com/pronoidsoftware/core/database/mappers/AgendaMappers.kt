package com.pronoidsoftware.core.database.mappers

import com.pronoidsoftware.core.database.entity.AttendeeEntity
import com.pronoidsoftware.core.database.entity.EventEntity
import com.pronoidsoftware.core.database.entity.EventWithAttendeesAndPhotos
import com.pronoidsoftware.core.database.entity.PhotoEntity
import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.database.entity.TaskEntity
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.util.toLocalDateTime
import com.pronoidsoftware.core.domain.util.toMillis

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
        id = id,
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
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toMillis(),
        notificationDateTime = notificationDateTime.toMillis(),
        isCompleted = isCompleted,
    )
}

fun EventWithAttendeesAndPhotos.toEvent(): AgendaItem.Event {
    return AgendaItem.Event(
        id = event.id,
        title = event.title,
        description = event.description,
        startDateTime = event.startDateTime.toLocalDateTime(),
        endDateTime = event.endDateTime.toLocalDateTime(),
        notificationDateTime = event.notificationDateTime.toLocalDateTime(),
        attendees = attendees.map { it.toAttendee() },
        photos = photos.map { it.toPhoto() },
        isLocalUserGoing = event.isLocalUserGoing,
        host = event.host,
        deletedPhotos = emptyList(),
        isUserEventCreator = event.isUserEventCreator,
    )
}

fun AttendeeEntity.toAttendee(): Attendee {
    return Attendee(
        userId = userId,
        fullName = fullName,
        isGoing = isGoing,
        remindAt = remindAt.toLocalDateTime(),
    )
}

fun PhotoEntity.toPhoto(): Photo {
    return Photo.Remote(
        key = key,
        url = url,
    )
}

fun AgendaItem.Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toMillis(),
        endDateTime = endDateTime.toMillis(),
        notificationDateTime = notificationDateTime.toMillis(),
        isLocalUserGoing = isLocalUserGoing,
        host = host,
        isUserEventCreator = isUserEventCreator,
    )
}

fun Attendee.toAttendeeEntity(eventId: String): AttendeeEntity {
    return AttendeeEntity(
        userId = userId,
        fullName = fullName,
        isGoing = isGoing,
        remindAt = remindAt.toMillis(),
        eventId = eventId,
    )
}

fun Photo.Remote.toPhotoEntity(eventId: String): PhotoEntity {
    return PhotoEntity(
        key = key,
        url = url,
        eventId = eventId,
    )
}
