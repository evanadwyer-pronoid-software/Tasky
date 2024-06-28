package com.pronoidsoftware.common.test.core.data.networking

import io.ktor.client.engine.HttpClientEngine
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class TaskyKtorTest {

    protected lateinit var mockEngine: HttpClientEngine

    @BeforeEach
    open fun setUp() {
        mockEngine = MockHttpClientEngine().get()
    }

    @AfterEach
    open fun tearDown() {
        mockEngine.close()
    }
}
