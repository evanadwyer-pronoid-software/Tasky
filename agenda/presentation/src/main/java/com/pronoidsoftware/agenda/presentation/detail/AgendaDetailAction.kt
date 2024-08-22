package com.pronoidsoftware.agenda.presentation.detail

import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import com.pronoidsoftware.core.domain.agendaitem.Photo
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface AgendaDetailAction {

    data object LoadEvent : AgendaDetailAction

    // toolbar actions
    data object OnClose : AgendaDetailAction
    data object OnConfirmClose : AgendaDetailAction
    data object OnCancelClose : AgendaDetailAction
    data object OnEnableEdit : AgendaDetailAction
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
    data class OnAddPhotoClick(val photo: Photo.Local) : AgendaDetailAction
    data class OnOpenPhotoClick(val photo: Photo) : AgendaDetailAction
    data object OnClosePhotoClick : AgendaDetailAction
    data class OnDeletePhotoClick(val photo: Photo) : AgendaDetailAction

    // time picker actions
    data object OnToggleStartTimePickerExpanded : AgendaDetailAction
    data class OnSelectStartTime(val startTime: LocalTime) : AgendaDetailAction
    data object OnToggleEndTimePickerExpanded : AgendaDetailAction
    data class OnSelectEndTime(val endTime: LocalTime) : AgendaDetailAction

    // date picker actions
    data object OnToggleStartDatePickerExpanded : AgendaDetailAction
    data class OnSelectStartDate(val startDate: LocalDate) : AgendaDetailAction
    data object OnToggleEndDatePickerExpanded : AgendaDetailAction
    data class OnSelectEndDate(val endDate: LocalDate) : AgendaDetailAction

    // notification actions
    data object OnToggleNotificationDurationExpanded : AgendaDetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : AgendaDetailAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean,
    ) : AgendaDetailAction

    data object DismissNotificationRationaleDialog : AgendaDetailAction

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
