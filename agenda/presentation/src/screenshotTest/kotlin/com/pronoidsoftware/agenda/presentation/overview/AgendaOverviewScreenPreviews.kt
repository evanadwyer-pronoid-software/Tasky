package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
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
                        AgendaOverviewItemUi(
                            id = "1",
                            type = AgendaItemType.TASK,
                            title = "Project X",
                            description = "Just work",
                            fromTime = "Mar 5, 10:00",
                            completed = true,
                        ),
                        AgendaOverviewItemUi(
                            id = "2",
                            type = AgendaItemType.EVENT,
                            title = "Meeting",
                            description = "Amet minim mollit non deserunt",
                            fromTime = "Mar 5, 10:30",
                            toTime = "Mar 5, 11:00",
                        ),
                        AgendaOverviewItemUi(
                            id = "3",
                            type = AgendaItemType.REMINDER,
                            title = "Lunch break",
                            description = "Just work",
                            fromTime = "Mar 5, 14:00",
                        ),
                        AgendaOverviewItemUi(
                            id = "4",
                            type = AgendaItemType.REMINDER,
                            title = "Meeting",
                            description = "Amet minim mollit non deserunt ullamco est",
                            fromTime = "Mar 5, 15:00",
                        ),
                    ),
                ),
                onAction = {},
            )
        }
    }
}
