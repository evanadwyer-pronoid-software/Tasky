package com.pronoidsoftware.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.domain.agendaitem.ReminderId

@Entity
data class DeletedReminderSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val reminderId: ReminderId,
    val userId: String,
)
