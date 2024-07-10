package com.pronoidsoftware.testutil.jvmtest.core.data.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class FixedClock(
    private val fixedInstant: Instant,
) : Clock {
    override fun now(): Instant {
        return fixedInstant
    }
}
