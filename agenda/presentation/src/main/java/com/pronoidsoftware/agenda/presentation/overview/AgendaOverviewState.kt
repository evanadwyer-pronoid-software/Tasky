package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.util.today
import kotlinx.datetime.LocalDate

data class AgendaOverviewState(
    val userInitials: String = "",
    val selectedDate: LocalDate = today(),
    val items: List<AgendaOverviewItemUi> = emptyList(),
    val profileDropdownMenuExpanded: Boolean = false,
    val fabDropdownMenuExpanded: Boolean = false,
    val datePickerExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isShowingDeleteConfirmationDialog: Boolean = false,
    val agendaTypeToDelete: AgendaItemType? = null,
    val eventToDeleteHostId: String? = null,
    val agendaItemIdToDelete: String = "",
)
