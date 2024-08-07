package com.pronoidsoftware.agenda.network.request

import kotlinx.serialization.Serializable

@Serializable
data class UpsertReminderRequest(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
)
