package com.pronoidsoftware.core.presentation.ui

import com.pronoidsoftware.core.domain.util.today
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun LocalDate.formatRelativeDate(clock: Clock = Clock.System): UiText {
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

fun LocalDate.formatFullDate(clock: Clock = Clock.System): UiText {
    val thisYear = this.year
    return UiText.DynamicString(
        this.format(
            LocalDate.Format {
                dayOfMonth()
                char(' ')
                monthName(MonthNames.ENGLISH_FULL)
                if (thisYear != today(clock).year) {
                    char(' ')
                    year()
                }
            },
        ).uppercase(),
    )
}

fun LocalTime.formatHours(): UiText {
    return UiText.DynamicString(
        this.format(
            LocalTime.Format {
                hour()
                char(':')
                minute()
            },
        ),
    )
}

fun LocalDateTime.formatOverview(): UiText {
    return UiText.DynamicString(
        this.format(
            LocalDateTime.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                dayOfMonth(padding = Padding.NONE)
                chars(", ")
                hour(padding = Padding.NONE)
                char(':')
                minute()
            },
        ),
    )
}
