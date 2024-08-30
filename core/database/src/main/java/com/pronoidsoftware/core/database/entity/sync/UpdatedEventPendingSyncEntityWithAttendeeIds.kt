package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Relation

data class UpdatedEventPendingSyncEntityWithAttendeeIds(
    @Embedded val updatedEventPendingSyncEntity: UpdatedEventPendingSyncEntity,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId",
    )
    val attendees: List<UpdatedEventPendingSyncAttendeeEntity>,
)
