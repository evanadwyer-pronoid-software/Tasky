package com.pronoidsoftware.agenda.presentation.detail

import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface AgendaDetailAction {
    // toolbar actions
    data object OnClose : AgendaDetailAction
    data object OnConfirmClose : AgendaDetailAction
    data object OnCancelClose : AgendaDetailAction
    data object OnEnableEdit : AgendaDetailAction
    data object OnDisableEdit : AgendaDetailAction
    data object OnSave : AgendaDetailAction

    // title actions
    data object OnEditTitle : AgendaDetailAction
    data object OnCloseTitle : AgendaDetailAction
    data object OnConfirmCloseTitle : AgendaDetailAction
    data object OnCancelCloseTitle : AgendaDetailAction
    data class OnSaveTitle(val newTitle: String) : AgendaDetailAction

    // description actions
    data object OnEditDescription : AgendaDetailAction
    data object OnCloseDescription : AgendaDetailAction
    data object OnConfirmCloseDescription : AgendaDetailAction
    data object OnCancelCloseDescription : AgendaDetailAction
    data class OnSaveDescription(val newDescription: String) : AgendaDetailAction

    // time picker actions
    data object OnToggleTimePickerExpanded : AgendaDetailAction
    data class OnSelectTime(
        val time: LocalTime,
    ) : AgendaDetailAction

    // date picker actions
    data object OnToggleDatePickerExpanded : AgendaDetailAction
    data class OnSelectDate(
        val date: LocalDate,
    ) : AgendaDetailAction

    // notification duration actions
    data object OnToggleNotificationDurationExpanded : AgendaDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : AgendaDetailAction

    // delete actions
    data object OnDelete : AgendaDetailAction
    data object OnConfirmDelete : AgendaDetailAction
    data object OnCancelDelete : AgendaDetailAction
}
