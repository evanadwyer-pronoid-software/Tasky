package com.pronoidsoftware.agenda.presentation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.pronoidsoftware.agenda.presentation.di.ClockModule
import com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewScreenRobot
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.tasky.MainActivity
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Rule
import org.junit.Test

@UninstallModules(
    ClockModule::class,
)
@HiltAndroidTest
class AgendaOverviewFixedDateTest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Module
    @InstallIn(SingletonComponent::class)
    object MutableClockModule {

        @Provides
        @Singleton
        fun provideClock(): Clock {
            val testTime = LocalDateTime(2023, 3, 5, 10, 15)
            return TestClock(testTime.toInstant(TimeZone.currentSystemDefault()))
        }
    }

    @Test
    fun testAgendaOverview_DateManipulation() {
        AgendaOverviewScreenRobot(composeRule)
            .assertMonthIsDisplayed("MARCH")
            .assertTodayIsDisplayed()
            .assertDateWidgetDateIsSelected(5)
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Sunday, March 5, 2023")
            .clickCancel()
            .assertTodayIsDisplayed()
            .assertDateWidgetDateIsSelected(5)
            .clickDatePicker()
            .clickDatePickerDate("Saturday, March 4, 2023")
            .clickConfirm()
            .assertDateWidgetDateIsSelected(4)
            .assertYesterdayIsDisplayed()
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Saturday, March 4, 2023")
            .clickDatePickerDate("Monday, March 6, 2023")
            .clickConfirm()
            .assertDateWidgetDateIsSelected(6)
            .assertTomorrowIsDisplayed()
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Monday, March 6, 2023")
            .clickDatePickerToday()
            .assertDateWidgetDateIsSelected(5)
            .assertTodayIsDisplayed()
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Sunday, March 5, 2023")
            .clickDatePickerDate("Friday, March 17, 2023")
            .clickConfirm()
            .assertDateWidgetDateIsSelected(17)
            .assertDateIsDisplayed("Mar 17")
            .clickDatePicker()
            .clickDatePickerDate("Wednesday, March 1, 2023")
            .clickCancel()
            .assertDateWidgetDateIsSelected(17)
            .assertDateIsDisplayed("Mar 17")
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Friday, March 17, 2023")
            .clickDatePickerToday()
            .scrollToDateWidgetDate(31)
            .assertTodayIsDisplayed()
            .clickDateWidgetDate(31)
            .assertDateIsDisplayed("Mar 31")
            .assertDateWidgetDateIsSelected(31)
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Friday, March 31, 2023")
            .clickCancel()
            .scrollToDateWidgetDate(1)
            .assertDateIsDisplayed("Mar 31")
            .assertDateWidgetDateIsSelectedNotDisplayed(31)
            .clickDateWidgetDate(1)
            .assertDateIsDisplayed("Mar 1")
            .clickDateWidgetDate(5)
            .assertTodayIsDisplayed()
            .clickDatePicker()
            .changeDatePickerToPreviousMonth()
            .clickDatePickerDate("Thursday, February 9, 2023")
            .clickConfirm()
            .assertMonthIsDisplayed("FEBRUARY")
            .assertDateWidgetDateIsSelected(9)
            .clickDatePicker()
            .assertDatePickerDateIsSelected("Thursday, February 9, 2023")
            .assertDateIsDisplayed("Feb 9")
            .changeDatePickerToNextMonth()
            .changeDatePickerToNextMonth()
            .clickDatePickerDate("Sunday, April 9, 2023")
            .clickConfirm()
            .assertDateWidgetDateIsSelected(9)
            .assertDateIsDisplayed("Apr 9")
            .clickDatePicker()
            .changeDatePickerToPreviousMonth()
            .changeDatePickerToPreviousMonth()
            .changeDatePickerToPreviousMonth()
            .changeDatePickerToPreviousMonth()
            .clickDatePickerDate("Sunday, December 25, 2022")
            .clickConfirm()
            .assertMonthIsDisplayed("DECEMBER")
            .assertDateIsDisplayed("Dec 25, 22")
    }
}
