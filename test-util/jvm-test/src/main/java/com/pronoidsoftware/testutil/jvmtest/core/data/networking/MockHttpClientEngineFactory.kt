package com.pronoidsoftware.testutil.jvmtest.core.data.networking

import com.pronoidsoftware.testutil.jvmtest.TestConstants
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class MockHttpClientEngineFactory {
    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    fun create(): HttpClientEngine {
        return MockEngine { request ->
            val body = request.body.toByteArray().decodeToString()
            val json = Json.parseToJsonElement(body).jsonObject
            var content = ""
            var status = HttpStatusCode.OK
            when (request.url.encodedPath) {
                "/register" -> {
                    val email = json["email"]?.jsonPrimitive?.content
                    if (email == TestConstants.CONFLICT_EMAIL) {
                        content = "/registerErrorRS.json"
                        status = HttpStatusCode.Conflict
                    } else if (email == TestConstants.VALID_EMAIL) {
                        content = "/registerSuccessRS.json"
                    }
                }

                "/login" -> {
                    val email = json["email"]?.jsonPrimitive?.content
                    val password = json["password"]?.jsonPrimitive?.content
                    if (
                        email == TestConstants.VALID_EMAIL &&
                        password == TestConstants.VALID_PASSWORD
                    ) {
                        content = "/loginSuccessRS.json"
                    } else {
                        content = "/loginErrorRS.json"
                        status = HttpStatusCode.Unauthorized
                    }
                }

                "/accessToken" -> {
                    val userId = json["userId"]?.jsonPrimitive?.content
                    if (userId == TestConstants.USER_ID) {
                        content = "/accessTokenSuccessRS.json"
                    } else if (userId == "") {
                        content = "/accessTokenErrorRS.json"
                        status = HttpStatusCode.Unauthorized
                    }
                }

                else -> {
                    error("Unhandled API call to ${request.url.encodedPath}")
                }
            }

            respond(
                content = object {}.javaClass.getResource(content)?.readText() ?: "{}",
                status = status,
                headers = responseHeaders,
            )
        }
    }
}
