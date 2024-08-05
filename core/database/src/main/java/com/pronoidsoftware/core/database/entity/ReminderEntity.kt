package com.pronoidsoftware.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String?,
    val startDateTime: Long,
    val notificationDateTime: Long,
)
