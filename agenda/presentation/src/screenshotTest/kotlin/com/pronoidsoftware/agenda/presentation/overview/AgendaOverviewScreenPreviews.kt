package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemContents
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.testutil.jvmtest.core.data.time.TestClock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

@Preview
@Composable
private fun AgendaOverviewScreenPreview_Figma() {
    TaskyTheme {
        val selectedDate = LocalDate(2023, 3, 5)
        val fixedClock = TestClock(selectedDate.atStartOfDayIn(TimeZone.currentSystemDefault()))
        CompositionLocalProvider(LocalClock provides fixedClock) {
            AgendaOverviewScreen(
                state = AgendaOverviewState(
                    userInitials = "AB",
                    selectedDate = today(fixedClock),
                    items = listOf(
                        AgendaOverviewItemUi.Item(
                            id = "1",
                            item = AgendaOverviewItemContents.TaskOverviewUiContents(
                                id = "1",
                                title = "Project X",
                                description = "Just work",
                                fromTime = "Mar 5, 10:00",
                                completed = true,
                            ),
                        ),
                        AgendaOverviewItemUi.TimeMarker(),
                        AgendaOverviewItemUi.Item(
                            id = "2",
                            item = AgendaOverviewItemContents.EventOverviewUiContents(
                                id = "2",
                                title = "Meeting",
                                description = "Amet minim mollit non deserunt",
                                fromTime = "Mar 5, 10:30",
                                toTime = "Mar 5, 11:00",
                            ),
                        ),
                        AgendaOverviewItemUi.Item(
                            id = "3",
                            item = AgendaOverviewItemContents.ReminderOverviewUiContents(
                                id = "3",
                                title = "Lunch break",
                                description = "Just work",
                                fromTime = "Mar 5, 14:00",
                            ),
                        ),
                        AgendaOverviewItemUi.Item(
                            id = "4",
                            item = AgendaOverviewItemContents.ReminderOverviewUiContents(
                                id = "4",
                                title = "Meeting",
                                description = "Amet minim mollit non deserunt ullamco est",
                                fromTime = "Mar 5, 15:00",
                            ),
                        ),
                    ),
                ),
                onAction = {},
            )
        }
    }
}
