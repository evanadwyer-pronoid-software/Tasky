package com.pronoidsoftware.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderPendingSyncEntity(
    @Embedded val reminder: ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String = reminder.id,
    val userId: String,
)
