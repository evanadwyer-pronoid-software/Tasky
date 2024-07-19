package com.pronoidsoftware.agenda.presentation.detail.reminder

import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface ReminderDetailAction {
    // toolbar actions
    data object OnClose : ReminderDetailAction
    data object OnConfirmClose : ReminderDetailAction
    data object OnCancelClose : ReminderDetailAction
    data object OnEnableEdit : ReminderDetailAction
    data object OnSave : ReminderDetailAction

    // title actions
    data object OnEditTitle : ReminderDetailAction
    data object OnCloseTitle : ReminderDetailAction
    data object OnConfirmCloseTitle : ReminderDetailAction
    data object OnCancelCloseTitle : ReminderDetailAction
    data class OnSaveTitle(val newTitle: String) : ReminderDetailAction

    // description actions
    data object OnEditDescription : ReminderDetailAction
    data object OnCloseDescription : ReminderDetailAction
    data object OnConfirmCloseDescription : ReminderDetailAction
    data object OnCancelCloseDescription : ReminderDetailAction
    data class OnSaveDescription(val newDescription: String) : ReminderDetailAction

    // time picker actions
    data object OnToggleTimePickerExpanded : ReminderDetailAction
    data class OnSelectTime(
        val time: LocalTime,
    ) : ReminderDetailAction

    // date picker actions
    data object OnToggleDatePickerExpanded : ReminderDetailAction
    data class OnSelectDate(
        val date: LocalDate,
    ) : ReminderDetailAction

    // notification duration actions
    data object OnToggleNotificationDurationExpanded : ReminderDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : ReminderDetailAction

    // delete actions
    data object OnDelete : ReminderDetailAction
    data object OnConfirmDelete : ReminderDetailAction
    data object OnCancelDelete : ReminderDetailAction
}
