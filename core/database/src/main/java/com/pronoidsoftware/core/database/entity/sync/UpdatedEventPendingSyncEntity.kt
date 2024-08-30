package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.database.entity.EventEntity
import com.pronoidsoftware.core.domain.agendaitem.EventId

@Entity
data class UpdatedEventPendingSyncEntity(
    @Embedded val event: EventEntity,
    @PrimaryKey(autoGenerate = false)
    val eventId: EventId = event.id,
    val userId: String,
)

@Entity(primaryKeys = ["attendeeId", "eventId"])
data class UpdatedEventPendingSyncAttendeeEntity(
    val eventId: EventId,
    val attendeeId: String,
)
