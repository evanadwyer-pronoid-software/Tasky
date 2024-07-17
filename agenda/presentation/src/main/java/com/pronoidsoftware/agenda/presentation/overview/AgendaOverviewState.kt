package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.util.today
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

data class AgendaOverviewState(
    val userInitials: String = "",
    val clock: Clock = Clock.System,
    val selectedDate: LocalDate = today(clock),
    val items: List<AgendaOverviewItemUi> = emptyList(),
)
