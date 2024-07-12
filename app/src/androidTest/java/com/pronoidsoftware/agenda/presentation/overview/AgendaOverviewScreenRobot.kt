package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.pronoidsoftware.TaskyComposeRule

class AgendaOverviewScreenRobot(
    private val composeRule: TaskyComposeRule,
) {

    fun clickDatePicker(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithContentDescription("Select date").performClick()
        return this
    }

    fun clickDateWidgetDate(dayOfMonth: Int): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText(dayOfMonth.toString()).performClick()
        return this
    }

    fun clickDatePickerDate(date: String): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText(date).performClick()
        return this
    }

    fun clickDatePickerToday(): AgendaOverviewScreenRobot {
        composeRule
            .onNode(hasText("Today") and hasAnySibling(hasText("Cancel")))
            .performClick()
        return this
    }

    fun clickCancel(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText("Cancel").performClick()
        return this
    }

    fun clickConfirm(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText("Confirm").performClick()
        return this
    }

    fun assertTodayIsDisplayed(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText("Today").isDisplayed()
        return this
    }

    fun assertYesterdayIsDisplayed(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText("Yesterday").isDisplayed()
        return this
    }

    fun assertTomorrowIsDisplayed(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText("Tomorrow").isDisplayed()
        return this
    }

    fun assertMonthIsDisplayed(month: String): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText(month).assertIsDisplayed()
        return this
    }

    fun assertDateIsDisplayed(date: String): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText(date).assertIsDisplayed()
        return this
    }

    fun assertDateWidgetDateIsSelected(dayOfMonth: Int): AgendaOverviewScreenRobot {
        composeRule
            .onNode(
                hasContentDescription("Selected date")
                    and hasText(dayOfMonth.toString(), substring = true),
            )
            .assertIsDisplayed()
        return this
    }

    fun assertDateWidgetDateIsSelectedNotDisplayed(dayOfMonth: Int): AgendaOverviewScreenRobot {
        composeRule
            .onNode(
                hasContentDescription("Selected date")
                    and hasText(dayOfMonth.toString(), substring = true),
            )
            .assertIsNotDisplayed()
        return this
    }

    fun assertDatePickerDateIsSelected(date: String): AgendaOverviewScreenRobot {
        composeRule.onNodeWithText(date).assertIsSelected()
        return this
    }

    fun scrollToDateWidgetDate(date: Int): AgendaOverviewScreenRobot {
        composeRule.onAllNodes(hasScrollToIndexAction()).onFirst().performScrollToIndex(date - 1)
        return this
    }

    fun changeDatePickerToPreviousMonth(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithContentDescription("Change to previous month").performClick()
        return this
    }

    fun changeDatePickerToNextMonth(): AgendaOverviewScreenRobot {
        composeRule.onNodeWithContentDescription("Change to next month").performClick()
        return this
    }
}
