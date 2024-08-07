package com.pronoidsoftware.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val startDateTime: Long,
    val notificationDateTime: Long,
)
