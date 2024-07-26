package com.pronoidsoftware.agenda.presentation.detail.reminder

sealed interface DetailEvent {
    data object OnSaved : DetailEvent
    data object OnDeleted : DetailEvent
    data object OnClosed : DetailEvent
}
