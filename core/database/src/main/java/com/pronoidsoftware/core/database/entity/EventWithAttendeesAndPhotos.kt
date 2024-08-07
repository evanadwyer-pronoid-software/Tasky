package com.pronoidsoftware.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithAttendeesAndPhotos(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId",
    )
    val attendees: List<AttendeeEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "eventId",
    )
    val photos: List<PhotoEntity>,
)
