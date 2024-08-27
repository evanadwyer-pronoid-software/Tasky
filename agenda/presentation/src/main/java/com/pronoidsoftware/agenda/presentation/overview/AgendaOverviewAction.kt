package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import kotlinx.datetime.LocalDate

sealed interface AgendaOverviewAction {
    data class OnTickClick(val id: String) : AgendaOverviewAction
    data class OnOpenClick(
        val type: AgendaItemType,
        val id: String,
    ) : AgendaOverviewAction

    data class OnEditClick(
        val type: AgendaItemType,
        val id: String,
    ) : AgendaOverviewAction

    data class OnDeleteClick(
        val type: AgendaItemType,
        val id: String,
        val eventHostId: String?,
    ) : AgendaOverviewAction
    data object OnConfirmDelete : AgendaOverviewAction
    data object OnCancelDelete : AgendaOverviewAction

    data class OnCreateClick(val type: AgendaItemType) : AgendaOverviewAction
    data object OnLogoutClick : AgendaOverviewAction
    data class OnSelectDate(val date: LocalDate) : AgendaOverviewAction
    data object OnToggleProfileDropdownMenu : AgendaOverviewAction
    data object OnToggleFABDropdownMenuExpanded : AgendaOverviewAction
    data object OnToggleDatePickerExpanded : AgendaOverviewAction
}
