package com.pronoidsoftware.agenda.presentation.overview

import com.pronoidsoftware.core.domain.agenda.AgendaItem

sealed interface AgendaOverviewEvent {
    data class Open(val id: String) : AgendaOverviewEvent
    data class Edit(val id: String) : AgendaOverviewEvent
    data class Delete(val id: String) : AgendaOverviewEvent
    data object Logout : AgendaOverviewEvent
    data class Create(val type: AgendaItem) : AgendaOverviewEvent
}
