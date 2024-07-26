package com.pronoidsoftware.agenda.presentation.detail.reminder

import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface DetailAction {
    // toolbar actions
    data object OnClose : DetailAction
    data object OnConfirmClose : DetailAction
    data object OnCancelClose : DetailAction
    data object OnEnableEdit : DetailAction
    data object OnSave : DetailAction

    // title actions
    data object OnEditTitle : DetailAction
    data object OnCloseTitle : DetailAction
    data object OnConfirmCloseTitle : DetailAction
    data object OnCancelCloseTitle : DetailAction
    data class OnSaveTitle(val newTitle: String) : DetailAction

    // description actions
    data object OnEditDescription : DetailAction
    data object OnCloseDescription : DetailAction
    data object OnConfirmCloseDescription : DetailAction
    data object OnCancelCloseDescription : DetailAction
    data class OnSaveDescription(val newDescription: String) : DetailAction

    // photo actions
    data class OnAddPhotoClick(
        val photo: PhotoId,
    ) : DetailAction

    data class OnOpenPhotoClick(
        val photo: PhotoId,
    ) : DetailAction

    //    data class OnEditPhotoClick(
//        val photo: PhotoId
//    ) : DetailAction
    data object OnClosePhotoClick : DetailAction
    data class OnDeletePhotoClick(
        val photo: PhotoId,
    ) : DetailAction

    // time picker actions
    data object OnToggleFromTimePickerExpanded : DetailAction
    data class OnSelectFromTime(
        val fromTime: LocalTime,
    ) : DetailAction

    data object OnToggleToTimePickerExpanded : DetailAction
    data class OnSelectToTime(
        val toTime: LocalTime,
    ) : DetailAction

    // date picker actions
    data object OnToggleFromDatePickerExpanded : DetailAction
    data class OnSelectFromDate(
        val fromDate: LocalDate,
    ) : DetailAction

    data object OnToggleToDatePickerExpanded : DetailAction
    data class OnSelectToDate(
        val toDate: LocalDate,
    ) : DetailAction

    // notification duration actions
    data object OnToggleNotificationDurationExpanded : DetailAction
    data class OnSelectNotificationDuration(
        val notificationDuration: NotificationDuration,
    ) : DetailAction

    // visitor actions
    data object OnAllVisitorsClick : DetailAction
    data object OnGoingVisitorsClick : DetailAction
    data object OnNotGoingVisitorsClick : DetailAction
    data object OnToggleAddVisitorDialog : DetailAction
    data class OnAddVisitorClick(
        val email: String,
    ) : DetailAction

    data class OnDeleteVisitorClick(
        val visitor: VisitorUI,
    ) : DetailAction

    // delete actions
    data object OnDelete : DetailAction
    data object OnConfirmDelete : DetailAction
    data object OnCancelDelete : DetailAction
}
