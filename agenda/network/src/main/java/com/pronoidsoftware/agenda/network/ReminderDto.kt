package com.pronoidsoftware.agenda.network

import kotlinx.serialization.Serializable

@Serializable
data class ReminderDto(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
)
