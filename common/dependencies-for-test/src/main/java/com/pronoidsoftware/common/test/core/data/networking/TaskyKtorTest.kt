package com.pronoidsoftware.common.test.core.data.networking

import io.ktor.client.engine.HttpClientEngine
import org.junit.After
import org.junit.Before

abstract class TaskyKtorTest {

    protected lateinit var mockEngine: HttpClientEngine

    @Before
    open fun setUp() {
        mockEngine = MockHttpClientEngine().get()
    }

    @After
    open fun tearDown() {
        mockEngine.close()
    }
}
