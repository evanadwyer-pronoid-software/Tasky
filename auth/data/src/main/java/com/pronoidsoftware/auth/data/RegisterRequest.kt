package com.pronoidsoftware.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
)
