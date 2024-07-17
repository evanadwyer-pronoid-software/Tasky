package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.agenda.domain.AgendaItem
import kotlinx.datetime.LocalDate

sealed interface AgendaOverviewAction {
    data class OnTickClick(val id: String) : AgendaOverviewAction
    data class OnOpenClick(val id: String) : AgendaOverviewAction
    data class OnEditClick(val id: String) : AgendaOverviewAction
    data class OnDeleteClick(val id: String) : AgendaOverviewAction
    data class OnCreateClick(val type: AgendaItem) : AgendaOverviewAction
    data object OnLogoutClick : AgendaOverviewAction
    data class OnSelectDate(val date: LocalDate) : AgendaOverviewAction
    data object OnToggleProfileDropdownMenu : AgendaOverviewAction
    data object OnToggleFABDropdownMenuExpanded : AgendaOverviewAction
    data object OnToggleDatePickerExpanded : AgendaOverviewAction
}
