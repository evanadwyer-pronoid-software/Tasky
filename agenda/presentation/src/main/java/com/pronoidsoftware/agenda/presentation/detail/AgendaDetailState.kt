package com.pronoidsoftware.agenda.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.today
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class AgendaDetailState(
    val selectedDate: LocalDate = today(),
    val isEditing: Boolean = false,
    val completed: Boolean = false,

    // title
    val title: String = "",
    val isEditingTitle: Boolean = false,

    // description
    val description: String? = null,
    val isEditingDescription: Boolean = false,

    // photos
    val photos: List<PhotoId> = emptyList(),
    val selectedPhotoToView: PhotoId? = null,

    // date time
    val startDateTime: LocalDateTime = now()
        .toInstant(TimeZone.currentSystemDefault())
        .plus(60.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val endDateTime: LocalDateTime = now()
        .toInstant(TimeZone.currentSystemDefault())
        .plus(90.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val isEditingStartTime: Boolean = false,
    val isEditingEndTime: Boolean = false,
    val isEditingStartDate: Boolean = false,
    val isEditingEndDate: Boolean = false,

    // notification
    val notificationDuration: NotificationDuration = NotificationDuration.Minutes30,
    val isEditingNotificationDuration: Boolean = false,

    // visitors
    val selectedVisitorFilter: VisitorFilterType = VisitorFilterType.ALL,
    val visitors: List<VisitorUI> = emptyList(),
    val isShowingAddVisitorDialog: Boolean = false,
    val isAddingVisitor: Boolean = false,
    val visitorToAddEmail: TextFieldState = TextFieldState(),
    val isVisitorToAddEmailValid: Boolean = false,
    val addVisitorErrorMessage: String = "",

    // confirmation dialogs
    val isShowingDeleteConfirmationDialog: Boolean = false,
    val isShowingCloseConfirmationDialog: Boolean = false,
) {
    val arePhotosFull: Boolean = photos.size >= MAX_PHOTOS

    companion object {
        const val MAX_PHOTOS = 10
    }
}
