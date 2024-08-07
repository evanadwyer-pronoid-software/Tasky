package com.pronoidsoftware.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val startDateTime: Long,
    val endDateTime: Long,
    val notificationDateTime: Long,
)

@Entity(primaryKeys = ["userId", "eventId"])
data class AttendeeEntity(
    val userId: String,
    val eventId: String,
    val email: String,
    val fullName: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

@Entity
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    val key: String,
    val url: String,
    val eventId: String,
)
