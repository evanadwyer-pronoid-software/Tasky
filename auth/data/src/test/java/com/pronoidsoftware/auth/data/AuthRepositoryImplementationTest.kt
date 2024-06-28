package com.pronoidsoftware.auth.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.core.data.networking.HttpClientFactory
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.testutil.jvmtest.TestConstants
import com.pronoidsoftware.testutil.jvmtest.core.data.auth.FakeSessionStorage
import com.pronoidsoftware.testutil.jvmtest.core.data.networking.MockHttpClientEngineFactory
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplementationTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var sessionStorage: SessionStorage
    private lateinit var httpClientEngine: HttpClientEngine

    @BeforeEach
    fun setUp() {
        sessionStorage = FakeSessionStorage()
        httpClientEngine = MockHttpClientEngineFactory().create()
        authRepository = AuthRepositoryImplementation(
            httpClient = HttpClientFactory(
                sessionStorage = sessionStorage,
                engine = httpClientEngine,
            )
                .build(),
            sessionStorage = sessionStorage,
        )
    }

    @AfterEach
    fun tearDown() {
        httpClientEngine.close()
    }

    @Test
    fun `register success`() = runBlocking {
        val response = authRepository.register(
            fullName = TestConstants.FULL_NAME,
            email = TestConstants.VALID_EMAIL,
            password = TestConstants.VALID_PASSWORD,
        )

        val expectedResponse = Result.Success(
            data = Unit,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun `register error - conflict`() = runBlocking {
        val response = authRepository.register(
            fullName = TestConstants.FULL_NAME,
            email = TestConstants.INVALID_EMAIL,
            password = TestConstants.VALID_PASSWORD,
        )

        val expectedResponse = Result.Error(
            error = DataError.Network.CONFLICT,
        )

        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun `login success - session storage updated`() = runBlocking {
        val response = authRepository.login(
            email = TestConstants.VALID_EMAIL,
            password = TestConstants.VALID_PASSWORD,
        )
        val authInfo = sessionStorage.get()

        val expectedResponse = Result.Success(
            data = Unit,
        )
        val expectedAuthInfo = AuthInfo(
            accessToken = TestConstants.ACCESS_TOKEN,
            refreshToken = TestConstants.REFRESH_TOKEN,
            userId = TestConstants.USER_ID,
            fullName = TestConstants.FULL_NAME,
        )

        assertThat(response).isEqualTo(expectedResponse)
        assertThat(authInfo).isEqualTo(expectedAuthInfo)
    }

    @Test
    fun `login error - wrong email - session storage not updated`() = runBlocking {
        val response = authRepository.login(
            email = TestConstants.INVALID_EMAIL,
            password = TestConstants.VALID_PASSWORD,
        )
        val authInfo = sessionStorage.get()

        val expectedResponse = Result.Error(
            error = DataError.Network.UNAUTHORIZED,
        )
        val expectedAuthInfo = null

        assertThat(response).isEqualTo(expectedResponse)
        assertThat(authInfo).isEqualTo(expectedAuthInfo)
    }

    @Test
    fun `login error - wrong password - session storage not updated`() = runBlocking {
        val response = authRepository.login(
            email = TestConstants.VALID_EMAIL,
            password = TestConstants.INVALID_PASSWORD,
        )
        val authInfo = sessionStorage.get()

        val expectedResponse = Result.Error(
            error = DataError.Network.UNAUTHORIZED,
        )
        val expectedAuthInfo = null

        assertThat(response).isEqualTo(expectedResponse)
        assertThat(authInfo).isEqualTo(expectedAuthInfo)
    }
}
