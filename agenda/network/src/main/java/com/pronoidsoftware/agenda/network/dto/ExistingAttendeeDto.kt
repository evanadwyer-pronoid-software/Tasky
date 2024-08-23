package com.pronoidsoftware.agenda.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetAttendeeDto(
    val doesUserExist: Boolean,
    val attendee: ExistingAttendee?,
)

@Serializable
data class ExistingAttendee(
    val email: String,
    val fullName: String,
    val userId: String,
)
