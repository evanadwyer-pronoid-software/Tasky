package com.pronoidsoftware.core.presentation.ui

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)
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

fun today(clock: Clock = Clock.System): LocalDate = clock.todayIn(TimeZone.UTC)

fun now(clock: Clock = Clock.System): LocalDateTime = clock.now().toLocalDateTime(TimeZone.UTC)

fun LocalDate.toMillis(): Long {
    return this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

fun Long.toLocalDate(): LocalDate {
    return Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date
}

fun LocalDate.toRelativeDate(clock: Clock = Clock.System): UiText {
    return when (this) {
        today(clock).plus(1, DateTimeUnit.DAY) -> UiText.StringResource(R.string.tomorrow)
        today(clock).minus(1, DateTimeUnit.DAY) -> UiText.StringResource(R.string.yesterday)
        today(clock) -> UiText.StringResource(R.string.today)
        else -> {
            val thisYear = this.year
            UiText.DynamicString(
                this.format(
                    LocalDate.Format {
                        monthName(MonthNames.ENGLISH_ABBREVIATED)
                        char(' ')
                        dayOfMonth(padding = Padding.NONE)
                        if (thisYear != today(clock).year) {
                            chars(", ")
                            yearTwoDigits(1996)
                        }
                    },
                ),
            )
        }
    }
}
