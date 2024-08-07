package com.pronoidsoftware.agenda.network.request

import kotlinx.serialization.Serializable

@Serializable
data class UpsertTaskRequest(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean,
)
