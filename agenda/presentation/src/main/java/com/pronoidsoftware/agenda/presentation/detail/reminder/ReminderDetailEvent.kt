package com.pronoidsoftware.agenda.presentation.detail.reminder

sealed interface ReminderDetailEvent {
    data object OnSaved : ReminderDetailEvent
    data object OnDeleted : ReminderDetailEvent
    data object OnClosed : ReminderDetailEvent
}
