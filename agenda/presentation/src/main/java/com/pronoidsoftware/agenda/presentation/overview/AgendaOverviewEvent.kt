package com.pronoidsoftware.agenda.presentation.overview

sealed interface AgendaOverviewEvent {
    data object OnLogout : AgendaOverviewEvent
}
