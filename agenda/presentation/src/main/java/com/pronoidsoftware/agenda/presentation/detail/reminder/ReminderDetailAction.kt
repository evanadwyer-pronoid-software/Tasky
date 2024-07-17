package com.pronoidsoftware.agenda.presentation.detail.reminder

import com.pronoidsoftware.agenda.presentation.model.NotificationDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface ReminderDetailAction {
    data object OnClose : ReminderDetailAction
    data object OnEnableEdit : ReminderDetailAction
    data object OnSave : ReminderDetailAction
    data object OnEditTitle : ReminderDetailAction
    data object OnEditDescription : ReminderDetailAction
    data object OnEditTime : ReminderDetailAction
    data class OnSelectTime(
        val time: LocalTime,
    ) : ReminderDetailAction

    data object OnEditDate : ReminderDetailAction
    data class OnSelectDate(
        val date: LocalDate,
    ) : ReminderDetailAction

    data object OnEditNotification : ReminderDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : ReminderDetailAction

    data object OnDelete : ReminderDetailAction
}
