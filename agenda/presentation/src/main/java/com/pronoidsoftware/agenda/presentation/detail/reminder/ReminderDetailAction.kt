package com.pronoidsoftware.agenda.presentation.detail.reminder

import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface ReminderDetailAction {
    data object OnClose : ReminderDetailAction
    data object OnEnableEdit : ReminderDetailAction
    data object OnSave : ReminderDetailAction
    data object OnEditTitle : ReminderDetailAction
    data object OnEditDescription : ReminderDetailAction
    data object OnToggleTimePickerExpanded : ReminderDetailAction
    data class OnSelectTime(
        val time: LocalTime,
    ) : ReminderDetailAction

    data object OnToggleDatePickerExpanded : ReminderDetailAction
    data class OnSelectDate(
        val date: LocalDate,
    ) : ReminderDetailAction

    data object OnToggleNotificationDurationExpanded : ReminderDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : ReminderDetailAction

    data object OnDelete : ReminderDetailAction
}
