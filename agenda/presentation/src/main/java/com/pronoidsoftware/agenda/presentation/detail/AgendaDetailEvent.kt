package com.pronoidsoftware.agenda.presentation.detail

import com.pronoidsoftware.core.presentation.ui.UiText

sealed interface AgendaDetailEvent {
    data object OnSaved : AgendaDetailEvent
    data object OnDeleted : AgendaDetailEvent
    data object OnClosed : AgendaDetailEvent
    data class OnError(val error: UiText) : AgendaDetailEvent
}
