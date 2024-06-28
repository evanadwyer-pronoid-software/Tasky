package com.pronoidsoftware.common.test.core.data.networking

import com.pronoidsoftware.core.domain.networking.AuthRoutes
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

class MockHttpClientEngine {
    fun get(): HttpClientEngine = engine

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    private val engine = MockEngine { request ->
        val body = request.body.toByteArray().decodeToString()
        val json = Json.parseToJsonElement(body).jsonObject
        var content = ""
        var status = HttpStatusCode.OK
        when (request.url.encodedPath) {
            AuthRoutes.REGISTER -> {
                val email = json["email"]?.jsonPrimitive?.content
                if (email == INVALID_EMAIL) {
                    content = "/registerErrorRS.json"
                    status = HttpStatusCode.Conflict
                } else if (email == VALID_EMAIL) {
                    content = "/registerSuccessRS.json"
                }
            }

            AuthRoutes.LOGIN -> {
                val email = json["email"]?.jsonPrimitive?.content
                val password = json["password"]?.jsonPrimitive?.content
                if (email == VALID_EMAIL && password == PASSWORD) {
                    content = "/loginSuccessRS.json"
                } else {
                    content = "/loginErrorRS.json"
                    status = HttpStatusCode.Unauthorized
                }
            }

            AuthRoutes.ACCESS_TOKEN -> {
                val userId = json["userId"]?.jsonPrimitive?.content
                if (userId == USER_ID) {
                    content = "/accessTokenSuccessRS.json"
                } else if (userId == "") {
                    content = "/accessTokenErrorRS.json"
                    status = HttpStatusCode.Unauthorized
                }
            }

            else -> {
                error("Unhandled")
            }
        }
        respond(
            content = object {}.javaClass.getResource(content)?.readText() ?: "{}",
            status = status,
            headers = responseHeaders,
        )
    }

    companion object {
        const val FULL_NAME = "Tester"
        const val VALID_EMAIL = "tester@test.com"
        const val INVALID_EMAIL = "hacker@hack.com"
        const val PASSWORD = "Tester123"
        const val USER_ID = "userId"
    }
}
