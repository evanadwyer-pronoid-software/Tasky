package com.pronoidsoftware.agenda.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AgendaDto(
    val events: List<EventDto>,
    val tasks: List<TaskDto>,
    val reminders: List<ReminderDto>,
)
