package com.pronoidsoftware.auth.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.common.test.core.data.auth.SessionStorageFake
import com.pronoidsoftware.common.test.core.data.networking.MockHttpClientEngine
import com.pronoidsoftware.common.test.core.data.networking.TaskyKtorTest
import com.pronoidsoftware.core.data.networking.HttpClientFactory
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplementationTest : TaskyKtorTest() {

    private lateinit var authRepository: AuthRepository
    private lateinit var sessionStorage: SessionStorage

    @Before
    override fun setUp() {
        super.setUp()
        sessionStorage = SessionStorageFake()
        authRepository = AuthRepositoryImplementation(
            httpClient = HttpClientFactory(
                sessionStorage = sessionStorage,
                engine = mockEngine,
            )
                .build(),
            sessionStorage = sessionStorage,
        )
    }

    @Test
    fun registerSuccess() = runBlocking {
        val response = authRepository.register(
            fullName = MockHttpClientEngine.FULL_NAME,
            email = MockHttpClientEngine.VALID_EMAIL,
            password = MockHttpClientEngine.PASSWORD,
        )

        val expectedResponse = Result.Success(
            data = Unit,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun registerError_Conflict() = runBlocking {
        val response = authRepository.register(
            fullName = MockHttpClientEngine.FULL_NAME,
            email = MockHttpClientEngine.INVALID_EMAIL,
            password = MockHttpClientEngine.PASSWORD,
        )

        val expectedResponse = Result.Error(
            DataError.Network.CONFLICT,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun loginSuccess_sharedPreferencesUpdated() = runBlocking {
        authRepository.login(
            email = MockHttpClientEngine.VALID_EMAIL,
            password = MockHttpClientEngine.PASSWORD,
        )

        val expectedAuthInfo = AuthInfo(
            accessToken = "testAccessToken",
            refreshToken = "testRefreshToken",
            userId = "1234a",
            fullName = "Tester",
        )

        assertThat(sessionStorage.get()).isEqualTo(expectedAuthInfo)
    }

    @Test
    fun loginError_wrongEmail_Unauthorized() = runBlocking {
        val response = authRepository.login(
            email = MockHttpClientEngine.VALID_EMAIL,
            password = "SomethingWrong1",
        )

        val expectedResponse = Result.Error(
            error = DataError.Network.UNAUTHORIZED,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun loginError_wrongPassword_Unauthorized() = runBlocking {
        val response = authRepository.login(
            email = "something@wrong.com",
            password = MockHttpClientEngine.PASSWORD,
        )

        val expectedResponse = Result.Error(
            error = DataError.Network.UNAUTHORIZED,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }
}
