package com.pronoidsoftware.core.domain.util

import kotlin.time.Duration
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.until

class DateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepDays: Long,
) : Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        val next = currentDate

        currentDate = currentDate.plus(stepDays, DateTimeUnit.DAY)

        return next
    }
}

class DateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1,
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive, stepDays)
}

fun LocalDate.startOfMonth(): LocalDate {
    return LocalDate(this.year, this.month, 1)
}

fun LocalDate.endOfMonth(): LocalDate {
    val startDate = this.startOfMonth()
    val end = startDate.plus(1, DateTimeUnit.MONTH)
    val daysInMonth = startDate.until(end, DateTimeUnit.DAY)
    return startDate.plus(daysInMonth - 1, DateTimeUnit.DAY)
}

operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

fun today(clock: Clock = Clock.System): LocalDate = clock.todayIn(TimeZone.currentSystemDefault())

fun now(clock: Clock = Clock.System): LocalDateTime =
    clock.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.toMillis(): Long {
    return this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    return this
        .toInstant(TimeZone.currentSystemDefault())
        .plus(duration)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.minus(duration: Duration): LocalDateTime {
    return this
        .toInstant(TimeZone.currentSystemDefault())
        .minus(duration)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
