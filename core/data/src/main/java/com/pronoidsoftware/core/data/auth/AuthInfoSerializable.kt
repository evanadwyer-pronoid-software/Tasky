package com.pronoidsoftware.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val fullName: String,
    val email: String,
)
