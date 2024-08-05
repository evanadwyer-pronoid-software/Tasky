package com.pronoidsoftware.core.domain.agendaitem

import kotlinx.datetime.LocalDateTime

data class Reminder(
    // null if new reminder
    val id: String?,
    val title: String,
    val description: String?,
    val startDateTime: LocalDateTime,
    val notificationDateTime: LocalDateTime,
)
