package com.pronoidsoftware.agenda.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val title: String,
    val description: String?,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photos: List<PhotoDto>,
)

@Serializable
data class AttendeeDto(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long,
)

@Serializable
data class PhotoDto(
    val key: String,
    val url: String,
)
