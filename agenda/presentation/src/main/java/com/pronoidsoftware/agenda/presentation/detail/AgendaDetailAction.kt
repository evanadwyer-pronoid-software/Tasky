package com.pronoidsoftware.agenda.presentation.detail

import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
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

    // photo actions
    data class OnAddPhotoClick(val photo: PhotoId) : AgendaDetailAction
    data class OnOpenPhotoClick(val photo: PhotoId) : AgendaDetailAction
    data object OnClockPhotoClick : AgendaDetailAction
    data class OnDeletePhotoClick(val photo: PhotoId) : AgendaDetailAction

    // time picker actions
    data object OnToggleFromTimePickerExpanded : AgendaDetailAction
    data class OnSelectFromTime(val fromTime: LocalTime) : AgendaDetailAction
    data object OnToggleToTimePickerExpanded : AgendaDetailAction
    data class OnSelectToTime(val toTime: LocalTime) : AgendaDetailAction

    // date picker actions
    data object OnToggleFromDatePickerExpanded : AgendaDetailAction
    data class OnSelectFromDate(val fromDate: LocalDate) : AgendaDetailAction
    data object OnToggleToDatePickerExpanded : AgendaDetailAction
    data class OnSelectToDate(val toDate: LocalDate) : AgendaDetailAction

    // notification duration actions
    data object OnToggleNotificationDurationExpanded : AgendaDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : AgendaDetailAction

    // visitor actions
    data object OnAllVisitorsClick : AgendaDetailAction
    data object OnGoingVisitorsClick : AgendaDetailAction
    data object OnNotGoingVisitorsClick : AgendaDetailAction
    data object OnToggleAddVisitorDialog : AgendaDetailAction
    data class OnAddVisitorClick(val email: String) : AgendaDetailAction
    data class OnDeleteVisitorClick(val visitor: VisitorUI) : AgendaDetailAction

    // delete actions
    data object OnDelete : AgendaDetailAction
    data object OnConfirmDelete : AgendaDetailAction
    data object OnCancelDelete : AgendaDetailAction
}
