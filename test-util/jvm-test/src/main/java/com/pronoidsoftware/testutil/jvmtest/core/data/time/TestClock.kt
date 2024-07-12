package com.pronoidsoftware.testutil.jvmtest.core.data.time

import kotlin.time.Duration
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TestClock(private var now: Instant) : Clock {
    private var offset: Duration = Duration.ZERO

    fun advanceTimeBy(duration: Duration) {
        offset = offset.plus(duration)
    }

    fun setNow(instant: Instant) {
        now = instant
        offset = Duration.ZERO
    }

    override fun now(): Instant {
        return now.plus(offset)
    }
}
