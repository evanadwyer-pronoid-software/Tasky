package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.domain.agendaitem.EventId

@Entity
data class DeletedEventSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val eventId: EventId,
    val userId: String,
)
