package com.pronoidsoftware.core.domain

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val fullName: String,
    val email: String,
)
