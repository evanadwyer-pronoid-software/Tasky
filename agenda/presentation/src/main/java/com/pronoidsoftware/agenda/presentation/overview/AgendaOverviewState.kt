package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaItemUi
import com.pronoidsoftware.core.domain.util.today
import kotlinx.datetime.LocalDate

data class AgendaOverviewState(
    val userInitials: String = "",
    val selectedDate: LocalDate = today(),
    val items: List<AgendaItemUi> = emptyList(),
    val profileDropdownMenuExpanded: Boolean = false,
    val fabDropdownMenuExpanded: Boolean = false,
    val datePickerExpanded: Boolean = false,
)
