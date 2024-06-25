package com.pronoidsoftware.auth.data

import com.pronoidsoftware.auth.data.login.LoginRequest
import com.pronoidsoftware.auth.data.login.LoginResponse
import com.pronoidsoftware.auth.data.register.RegisterRequest
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.core.data.networking.AuthRoutes
import com.pronoidsoftware.core.data.networking.post
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.asEmptyResult
import com.pronoidsoftware.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import javax.inject.Inject

class AuthRepositoryImplementation @Inject constructor(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage,
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

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> =
        httpClient.post<LoginRequest, LoginResponse>(
            route = AuthRoutes.LOGIN,
            body = LoginRequest(
                email = email,
                password = password,
            ),
        )
            .onSuccess {
                sessionStorage.set(
                    AuthInfo(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken,
                        userId = it.userId,
                        fullName = it.fullName,
                    ),
                )
            }
            .asEmptyResult()
}
