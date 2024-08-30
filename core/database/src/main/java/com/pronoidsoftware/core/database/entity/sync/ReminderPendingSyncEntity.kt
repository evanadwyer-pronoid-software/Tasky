package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.database.entity.ReminderEntity

@Entity
data class ReminderPendingSyncEntity(
    @Embedded val reminder: ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String = reminder.id,
    val userId: String,
)
