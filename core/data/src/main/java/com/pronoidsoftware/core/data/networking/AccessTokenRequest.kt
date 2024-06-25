package com.pronoidsoftware.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String,
)
