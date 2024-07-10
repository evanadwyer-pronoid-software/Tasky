package com.pronoidsoftware.testutil.jvmtest.core.data.time

import kotlin.time.Duration
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class MutableClock(private val delegate: Clock) : Clock {
    private var offset: Duration = Duration.ZERO

    fun advanceTimeBy(duration: Duration) {
        offset = offset.plus(duration)
    }

    override fun now(): Instant {
        return delegate.now().plus(offset)
    }
}
