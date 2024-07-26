package com.pronoidsoftware.core.presentation.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
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

class DateTimePresentationUtilTest {

    private lateinit var context: Context
    private lateinit var dateFormat: DateTimeFormat<LocalDate>
    private lateinit var clockNow: Clock
    private lateinit var timeZone: TimeZone

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        timeZone = TimeZone.currentSystemDefault()
        dateFormat = LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth(padding = Padding.NONE)
            chars(", ")
            yearTwoDigits(1996)
        }
        clockNow = TestClock(
            dateFormat.parse("Jul 7, 24").atStartOfDayIn(timeZone),
        )
    }

    @Test
    fun todayFormatted() {
        val date = today()
        val expectedFormat = "Today"
        val actualFormat = date.formatRelativeDate().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun yesterdayFormatted() {
        val date = today().minus(1, DateTimeUnit.DAY)
        val expectedFormat = "Yesterday"
        val actualFormat = date.formatRelativeDate().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun tomorrowFormatted() {
        val date = today().plus(1, DateTimeUnit.DAY)
        val expectedFormat = "Tomorrow"
        val actualFormat = date.formatRelativeDate().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun futureDateInYearFormatted() {
        val futureDate = LocalDate(2024, 12, 5)
        val expectedFormat = "Dec 5"
        val actualFormat = futureDate.formatRelativeDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun futureDateOutOfYearFormatted() {
        val futureDate = LocalDate(2025, 3, 5)
        val expectedFormat = "Mar 5, 25"
        val actualFormat = futureDate.formatRelativeDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun pastDateInYearFormatted() {
        val pastDate = LocalDate(2024, 3, 5)
        val expectedFormat = "Mar 5"
        val actualFormat = pastDate.formatRelativeDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun pastDateOutOfYearFormatted() {
        val pastDate = LocalDate(2023, 3, 5)
        val expectedFormat = "Mar 5, 23"
        val actualFormat = pastDate.formatRelativeDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun todayFormattedFull() {
        val today = LocalDate(2024, 3, 5)
        val expectedFormat = "05 MARCH"
        val actualFormat = today.formatFullDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun pastInYearFormattedFull() {
        val today = LocalDate(2024, 2, 19)
        val expectedFormat = "19 FEBRUARY"
        val actualFormat = today.formatFullDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun futureInYearFormattedFull() {
        val today = LocalDate(2024, 5, 31)
        val expectedFormat = "31 MAY"
        val actualFormat = today.formatFullDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun pastOutOfYearFormattedFull() {
        val today = LocalDate(2023, 3, 5)
        val expectedFormat = "05 MARCH 2023"
        val actualFormat = today.formatFullDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun futureOutOfYearFormattedFull() {
        val today = LocalDate(2025, 3, 5)
        val expectedFormat = "05 MARCH 2025"
        val actualFormat = today.formatFullDate(clockNow).asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun morningHourFormatted() {
        val now = LocalDateTime(2023, 3, 5, 8, 0)
        val expectedFormat = "08:00"
        val actualFormat = now.time.formatHours().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun startOfDayHourFormatted() {
        val now = LocalDate(2023, 3, 5)
            .atStartOfDayIn(timeZone)
            .toLocalDateTime(timeZone)
        val expectedFormat = "00:00"
        val actualFormat = now.time.formatHours().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun noonHourFormatted() {
        val now = LocalDateTime(2023, 3, 5, 12, 0)
        val expectedFormat = "12:00"
        val actualFormat = now.time.formatHours().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun afternoonHourFormatted() {
        val now = LocalDateTime(2023, 3, 5, 17, 30)
        val expectedFormat = "17:30"
        val actualFormat = now.time.formatHours().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }

    @Test
    fun tomorrowMidnightHourFormatted() {
        val now = LocalDateTime(2023, 3, 5, 23, 59)
            .toInstant(timeZone)
            .plus(1.minutes)
            .toLocalDateTime(timeZone)
        val expectedFormat = "00:00"
        val actualFormat = now.time.formatHours().asString(context)
        assertThat(actualFormat).isEqualTo(expectedFormat)
    }
}
