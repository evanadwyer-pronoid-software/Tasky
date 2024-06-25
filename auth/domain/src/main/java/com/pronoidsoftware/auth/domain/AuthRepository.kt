package com.pronoidsoftware.auth.domain

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): EmptyResult<DataError.Network>

    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
}
