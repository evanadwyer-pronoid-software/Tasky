package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.pronoidsoftware.core.database.entity.ReminderEntity

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
data class CreatedReminderPendingSyncEntity(
    @Embedded val reminder: ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String = reminder.id,
    val userId: String,
)
