package com.pronoidsoftware.agenda.presentation.detail

sealed interface AgendaDetailEvent {
    data object OnSaved : AgendaDetailEvent
    data object OnDeleted : AgendaDetailEvent
    data object OnClosed : AgendaDetailEvent
}
