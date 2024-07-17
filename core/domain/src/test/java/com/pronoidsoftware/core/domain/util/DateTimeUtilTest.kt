package com.pronoidsoftware.core.domain.util

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.junit.Before
import org.junit.Test

class DateTimeUtilTest {

    private lateinit var dateFormat: DateTimeFormat<LocalDate>
    private lateinit var clockNow: Clock

    @Before
    fun setup() {
        dateFormat = LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth(padding = Padding.NONE)
            chars(", ")
            yearTwoDigits(1996)
        }
        clockNow = TestClock(
            dateFormat.parse("Jul 7, 24").atStartOfDayIn(TimeZone.currentSystemDefault()),
        )
    }

    @Test
    fun numDaysInJuly2024() {
        val selectedDate = LocalDate(2024, 7, 10)
        val startDate = selectedDate.startOfMonth()
        val endDate = selectedDate.endOfMonth()
        val dateRange = startDate.rangeTo(endDate).toList()
        assertThat(dateRange).hasSize(31)
    }

    @Test
    fun numDaysInFebruary2024_leapYear() {
        val selectedDate = LocalDate(2024, 2, 9)
        val startDate = selectedDate.startOfMonth()
        val endDate = selectedDate.endOfMonth()
        val dateRange = startDate.rangeTo(endDate).toList()
        assertThat(dateRange).hasSize(29)
    }

    @Test
    fun numDaysInFebruary2025_notLeapYear() {
        val selectedDate = LocalDate(2025, 2, 9)
        val startDate = selectedDate.startOfMonth()
        val endDate = selectedDate.endOfMonth()
        val dateRange = startDate.rangeTo(endDate).toList()
        assertThat(dateRange).hasSize(28)
    }

    @Test
    fun conversionBetweenLocalDateAndMillis() {
        val today = today()
        val millis = today.toMillis()
        val todayFromMillis = millis.toLocalDate()
        assertThat(todayFromMillis).isEqualTo(today)
    }

    @Test
    fun conversionBetweenLocalDateAndMillisFuture() {
        val future = today().plus(1, DateTimeUnit.MONTH)
        val millis = future.toMillis()
        val futureFromMillis = millis.toLocalDate()
        assertThat(futureFromMillis).isEqualTo(future)
    }

    @Test
    fun conversionBetweenLocalDateAndMillisPast() {
        val past = today().minus(1, DateTimeUnit.MONTH)
        val millis = past.toMillis()
        val pastFromMillis = millis.toLocalDate()
        assertThat(pastFromMillis).isEqualTo(past)
    }

    @Test
    fun conversionBetweenLocalDateTimeAndMillis() {
        val now = now(clockNow)
        val millis = now.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val nowFromMillis = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        assertThat(nowFromMillis).isEqualTo(now)
    }
}
