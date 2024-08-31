package com.pronoidsoftware.agenda.network.mappers

import com.pronoidsoftware.agenda.network.dto.AttendeeDto
import com.pronoidsoftware.agenda.network.dto.EventDto
import com.pronoidsoftware.agenda.network.dto.PhotoDto
import com.pronoidsoftware.agenda.network.dto.ReminderDto
import com.pronoidsoftware.agenda.network.dto.TaskDto
import com.pronoidsoftware.agenda.network.request.CreateEventRequest
import com.pronoidsoftware.agenda.network.request.UpdateEventRequest
import com.pronoidsoftware.agenda.network.request.UpsertReminderRequest
import com.pronoidsoftware.agenda.network.request.UpsertTaskRequest
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.util.now
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

fun EventDto.toEvent(localUserId: String?): AgendaItem.Event {
    return AgendaItem.Event(
        id = id,
        title = title,
        description = description,
        startDateTime = from.toLocalDateTime(),
        endDateTime = to.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
        attendees = attendees.map { it.toAttendee() },
        photos = photos.map { it.toPhoto() },
        host = host,
        isUserEventCreator = isUserEventCreator,
        isLocalUserGoing = attendees.find { it.userId == localUserId }?.isGoing ?: false,
        deletedPhotos = emptyList(),
        deletedAttendees = emptyList(),
    )
}

fun AttendeeDto.toAttendee(): Attendee {
    return Attendee(
        userId = userId,
        email = email,
        fullName = fullName,
        isGoing = isGoing,
        remindAt = remindAt.toLocalDateTime(),
    )
}

fun PhotoDto.toPhoto(): Photo {
    return Photo.Remote(
        key = key,
        url = url,
    )
}

fun AgendaItem.Event.toCreateEventRequest(): CreateEventRequest {
    return CreateEventRequest(
        id = id,
        title = title,
        description = description,
        from = startDateTime.toMillis(),
        to = endDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
        attendeeIds = attendees.map { it.userId },
    )
}

// For handling offline first upload failures
fun CreateEventRequest.toEvent(localUserId: String): AgendaItem.Event {
    return AgendaItem.Event(
        id = id,
        title = title,
        description = description,
        startDateTime = from.toLocalDateTime(),
        endDateTime = to.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
        host = localUserId,
        isUserEventCreator = true,
        attendees = attendeeIds.map { it.toAttendee() },
        deletedAttendees = emptyList(),
        photos = emptyList(),
        deletedPhotos = emptyList(),
        isLocalUserGoing = true,
    )
}

fun AgendaItem.Event.toUpdateEventRequest(): UpdateEventRequest {
    return UpdateEventRequest(
        id = id,
        title = title,
        description = description,
        from = startDateTime.toMillis(),
        to = endDateTime.toMillis(),
        remindAt = notificationDateTime.toMillis(),
        attendeeIds = attendees.map { it.userId },
        deletedPhotoKeys = deletedPhotos.map { it.key },
        isGoing = isLocalUserGoing,
    )
}

fun UpdateEventRequest.toEvent(): AgendaItem.Event {
    return AgendaItem.Event(
        id = id,
        title = title,
        description = description,
        startDateTime = from.toLocalDateTime(),
        endDateTime = to.toLocalDateTime(),
        notificationDateTime = remindAt.toLocalDateTime(),
        host = "",
        isUserEventCreator = false,
        attendees = attendeeIds.map { it.toAttendee() },
        deletedAttendees = emptyList(),
        photos = emptyList(),
        deletedPhotos = emptyList(),
        isLocalUserGoing = isGoing,
    )
}

// Only need this to hold onto attendee IDs when updating event remotely
private fun String.toAttendee(): Attendee {
    return Attendee(
        userId = this,
        email = "",
        fullName = "",
        isGoing = true,
        remindAt = now(),
    )
}
