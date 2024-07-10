package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewListItem
import com.pronoidsoftware.core.presentation.ui.today
import kotlinx.datetime.LocalDate

data class AgendaOverviewState(
    val userInitials: String = "",
    val selectedDate: LocalDate = today(),
    val items: List<AgendaOverviewListItem> = emptyList(),
)
