package com.pronoidsoftware.agenda.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.plus
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.ui.UiText
import java.util.UUID
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class AgendaDetailState(
    val agendaItemId: String = "",
    val isCreateAgendaItem: Boolean = false,
    val agendaItemType: AgendaItemType? = null,
    val selectedDate: LocalDate = today(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,

    // title
    val title: String = "",
    val isEditingTitle: Boolean = false,

    // description
    val description: String? = null,
    val isEditingDescription: Boolean = false,

    // date time
    val startDateTime: LocalDateTime = now().plus(60.minutes),
    val isEditingStartTime: Boolean = false,
    val isEditingStartDate: Boolean = false,

    // notification
    val notificationDuration: NotificationDuration = NotificationDuration.Minutes30,
    val isEditingNotificationDuration: Boolean = false,
    val showNotificationRationale: Boolean = false,

    // confirmation dialogs
    val isShowingDeleteConfirmationDialog: Boolean = false,
    val isShowingCloseConfirmationDialog: Boolean = false,

    val typeSpecificDetails: AgendaItemDetails? = null,
)

sealed interface AgendaItemDetails {
    data class Event(
        val host: String = "",
        val isUserEventCreator: Boolean = false,
        val isLocalUserGoing: Boolean = false,
        val workId: UUID? = null,

        // photos
        val photos: List<Photo> = emptyList(),
        val selectedPhotoToView: Photo? = null,
        val deletedPhotos: List<Photo.Remote> = emptyList(),

        // date time
        val endDateTime: LocalDateTime = now().plus(90.minutes),
        val isEditingEndTime: Boolean = false,
        val isEditingEndDate: Boolean = false,

        // visitors
        val selectedVisitorFilter: VisitorFilterType = VisitorFilterType.ALL,
        val attendees: List<Attendee> = emptyList(),
        val isShowingAddVisitorDialog: Boolean = false,
        val isAddingVisitor: Boolean = false,
        val visitorToAddEmail: TextFieldState = TextFieldState(),
        val isVisitorToAddEmailValid: Boolean = false,
        val addVisitorErrorMessage: UiText? = null,
    ) : AgendaItemDetails {
        val arePhotosFull: Boolean = photos.size >= MAX_PHOTOS

        companion object {
            const val MAX_PHOTOS = 10
        }
    }

    data class Task(
        val completed: Boolean = false,
    ) : AgendaItemDetails

    data object Reminder : AgendaItemDetails
}
