package com.pronoidsoftware.auth.data

import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.core.data.networking.AuthRoutes
import com.pronoidsoftware.core.data.networking.post
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import io.ktor.client.HttpClient
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
    private val httpClient: HttpClient,
) : AuthRepository {

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
    ): EmptyResult<DataError.Network> = httpClient.post<RegisterRequest, Unit>(
        route = AuthRoutes.REGISTER,
        body = RegisterRequest(
            fullName = fullName,
            email = email,
            password = password,
        ),
    )
}
