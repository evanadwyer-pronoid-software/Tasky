package com.pronoidsoftware.agenda.network.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateEventRequest(
    val id: String,
    val title: String,
    val description: String?,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>,
    val deletedPhotoKeys: List<String>,
    val isGoing: Boolean,
)
