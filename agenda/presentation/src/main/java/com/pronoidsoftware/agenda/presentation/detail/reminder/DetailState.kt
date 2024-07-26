package com.pronoidsoftware.agenda.presentation.detail.reminder

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

data class DetailState(
    val selectedDate: LocalDate = today(),
    val title: String = "",
    val completed: Boolean = false,
    val description: String? = null,
    val photos: List<PhotoId> = emptyList(),
    val selectedPhoto: PhotoId? = null,
//    val isViewingPhoto: Boolean = false,
//    val isEditingPhoto: Boolean = false,
    val fromTime: LocalDateTime = now()
        .toInstant(TimeZone.currentSystemDefault())
        .plus(60.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val toTime: LocalDateTime = now()
        .toInstant(TimeZone.currentSystemDefault())
        .plus(90.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val notificationDuration: NotificationDuration = NotificationDuration.Minutes30,
    val selectedVisitorFilter: VisitorFilterType = VisitorFilterType.ALL,
    val visitors: List<VisitorUI> = emptyList(),
    val isEditing: Boolean = false,
    val isEditingTitle: Boolean = false,
    val isEditingDescription: Boolean = false,
    val isEditingFromTime: Boolean = false,
    val isEditingFromDate: Boolean = false,
    val isEditingToTime: Boolean = false,
    val isEditingToDate: Boolean = false,
    val isAddingVisitor: Boolean = false,
    val visitorToAddEmail: TextFieldState = TextFieldState(),
    val isVisitorToAddEmailValid: Boolean = false,
    val addVisitorErrorMessage: String = "",
    val isShowingAddVisitorDialog: Boolean = false,
    val isEditingNotificationDuration: Boolean = false,
    val isShowingDeleteConfirmationDialog: Boolean = false,
    val isShowingCloseConfirmationDialog: Boolean = false,
)
