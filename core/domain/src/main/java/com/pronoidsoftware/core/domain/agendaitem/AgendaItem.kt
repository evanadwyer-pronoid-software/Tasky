package com.pronoidsoftware.core.domain.agendaitem

import kotlinx.datetime.LocalDateTime

sealed class AgendaItem(
    open val id: String,
    open val title: String,
    open val description: String?,
    open val startDateTime: LocalDateTime,
    open val notificationDateTime: LocalDateTime,
) {
    data class Reminder(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val startDateTime: LocalDateTime,
        override val notificationDateTime: LocalDateTime,
    ) : AgendaItem(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
        notificationDateTime = notificationDateTime,
    )

    data class Task(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val startDateTime: LocalDateTime,
        override val notificationDateTime: LocalDateTime,
        val isCompleted: Boolean,
    ) : AgendaItem(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
        notificationDateTime = notificationDateTime,
    )

    data class Event(
        override val id: String,
        override val title: String,
        override val description: String?,
        override val startDateTime: LocalDateTime,
        override val notificationDateTime: LocalDateTime,
        val endDateTime: LocalDateTime,
        val host: String,
        val isUserEventCreator: Boolean,
        val attendees: List<Attendee>,
        val photos: List<Photo>,
        val deletedPhotos: List<Photo.Remote>,
        val isLocalUserGoing: Boolean,
    ) : AgendaItem(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
        notificationDateTime = notificationDateTime,
    )
}

data class Attendee(
    val userId: String,
    val email: String,
    val fullName: String,
    val isGoing: Boolean,
    val remindAt: LocalDateTime,
)

sealed interface Photo {
    data class Remote(
        val key: String,
        val url: String,
    ) : Photo

    data class Local(
        val localPhotoUri: String,
    ) : Photo
}
