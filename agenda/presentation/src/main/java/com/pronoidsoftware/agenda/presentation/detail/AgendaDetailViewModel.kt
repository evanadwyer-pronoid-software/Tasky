package com.pronoidsoftware.agenda.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.domain.validation.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

@HiltViewModel
class AgendaDetailViewModel @Inject constructor(
    clock: Clock,
    userDataValidator: UserDataValidator,
) : ViewModel() {

    var state by mutableStateOf(
        AgendaDetailState(
            selectedDate = today(clock),
            startDateTime = now(clock)
                .toInstant(TimeZone.currentSystemDefault())
                .plus(60.minutes)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
        ),
    )
        private set

    private val eventChannel = Channel<AgendaDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { state.visitorToAddEmail.text }
            .onEach { email ->
                state = state.copy(
                    isVisitorToAddEmailValid = userDataValidator.validateEmail(email.toString()),
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AgendaDetailAction) {
        when (action) {
            AgendaDetailAction.OnEnableEdit -> {
                state = state.copy(
                    isEditing = true,
                )
            }

            AgendaDetailAction.OnDisableEdit -> {
                state = state.copy(
                    isEditing = false,
                )
            }

            AgendaDetailAction.OnSave -> {
                viewModelScope.launch {
                    state = state.copy(
                        isEditing = false,
                    )
                    eventChannel.send(AgendaDetailEvent.OnSaved)
                }
            }

            AgendaDetailAction.OnEditTitle -> {
                state = state.copy(
                    isEditingTitle = true,
                )
            }

            AgendaDetailAction.OnCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            AgendaDetailAction.OnConfirmCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                    isEditingTitle = false,
                )
            }

            AgendaDetailAction.OnCancelCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            is AgendaDetailAction.OnSaveTitle -> {
                state = state.copy(
                    isEditingTitle = false,
                    title = action.newTitle,
                )
            }

            AgendaDetailAction.OnEditDescription -> {
                state = state.copy(
                    isEditingDescription = true,
                )
            }

            AgendaDetailAction.OnCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            AgendaDetailAction.OnConfirmCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                    isEditingDescription = false,
                )
            }

            AgendaDetailAction.OnCancelCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            is AgendaDetailAction.OnSaveDescription -> {
                state = state.copy(
                    isEditingDescription = false,
                    description = action.newDescription,
                )
            }

            is AgendaDetailAction.OnAddPhotoClick -> {
                state = state.copy(
                    photos = state.photos + action.photo,
                )
            }

            is AgendaDetailAction.OnOpenPhotoClick -> {
                state = state.copy(
                    selectedPhotoToView = action.photo,
                )
            }

            AgendaDetailAction.OnClosePhotoClick -> {
                state = state.copy(
                    selectedPhotoToView = null,
                )
            }

            is AgendaDetailAction.OnDeletePhotoClick -> {
                state = state.copy(
                    photos = state.photos.filterNot { it == action.photo },
                    selectedPhotoToView = null,
                )
            }

            is AgendaDetailAction.OnSelectStartDate -> {
                val date = action.startDate
                val newStartTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.startDateTime.hour,
                    state.startDateTime.minute,
                )
                val newEndDateTime = if (newStartTime > state.endDateTime) {
                    newStartTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.endDateTime
                }
                state = state.copy(
                    startDateTime = newStartTime,
                    endDateTime = newEndDateTime,
                )
            }

            is AgendaDetailAction.OnSelectStartTime -> {
                val time = action.startTime
                val newStartTime = LocalDateTime(
                    state.startDateTime.year,
                    state.startDateTime.month,
                    state.startDateTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newEndDateTime = if (newStartTime > state.endDateTime) {
                    newStartTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.endDateTime
                }
                state = state.copy(
                    startDateTime = newStartTime,
                    endDateTime = newEndDateTime,
                )
            }

            is AgendaDetailAction.OnSelectEndDate -> {
                val date = action.endDate
                val newEndDateTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.startDateTime.hour,
                    state.startDateTime.minute,
                )
                val newStartDateTime = if (newEndDateTime < state.startDateTime) {
                    newEndDateTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.startDateTime
                }
                state = state.copy(
                    startDateTime = newStartDateTime,
                    endDateTime = newEndDateTime,
                )
            }

            is AgendaDetailAction.OnSelectEndTime -> {
                val time = action.endTime
                val newEndDateTime = LocalDateTime(
                    state.startDateTime.year,
                    state.startDateTime.month,
                    state.startDateTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newStartDateTime = if (newEndDateTime < state.startDateTime) {
                    newEndDateTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.startDateTime
                }
                state = state.copy(
                    startDateTime = newStartDateTime,
                    endDateTime = newEndDateTime,
                )
            }

            AgendaDetailAction.OnToggleStartTimePickerExpanded -> {
                state = state.copy(
                    isEditingStartTime = !state.isEditingStartTime,
                )
            }

            AgendaDetailAction.OnToggleStartDatePickerExpanded -> {
                state = state.copy(
                    isEditingStartDate = !state.isEditingStartDate,
                )
            }

            AgendaDetailAction.OnToggleEndTimePickerExpanded -> {
                state = state.copy(
                    isEditingEndTime = !state.isEditingEndTime,
                )
            }

            AgendaDetailAction.OnToggleEndDatePickerExpanded -> {
                state = state.copy(
                    isEditingEndDate = !state.isEditingEndDate,
                )
            }

            AgendaDetailAction.OnToggleNotificationDurationExpanded -> {
                state = state.copy(
                    isEditingNotificationDuration = !state.isEditingNotificationDuration,
                )
            }

            is AgendaDetailAction.OnSelectNotificationDuration -> {
                state = state.copy(
                    notificationDuration = action.notificationDuration,
                )
            }

            AgendaDetailAction.OnAllVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.ALL,
                )
            }

            AgendaDetailAction.OnGoingVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.GOING,
                )
            }

            AgendaDetailAction.OnNotGoingVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.NOT_GOING,
                )
            }

            AgendaDetailAction.OnToggleAddVisitorDialog -> {
                state = state.copy(
                    visitorToAddEmail = TextFieldState(),
                    isShowingAddVisitorDialog = !state.isShowingAddVisitorDialog,
                )
            }

            is AgendaDetailAction.OnAddVisitorClick -> {
                viewModelScope.launch {
                    state = state.copy(
                        isAddingVisitor = true,
                    )
                    state = state.copy(
                        visitorToAddEmail = TextFieldState(),
                        isAddingVisitor = false,
                        isShowingAddVisitorDialog = false,
                    )
                }
            }

            is AgendaDetailAction.OnDeleteVisitorClick -> {
                state = state.copy(
                    visitors = state.visitors.filterNot { it == action.visitor },
                )
            }

            AgendaDetailAction.OnDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = true,
                )
            }

            AgendaDetailAction.OnConfirmDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = false,
                )
                viewModelScope.launch {
                    eventChannel.send(AgendaDetailEvent.OnDeleted)
                }
            }

            AgendaDetailAction.OnCancelDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = false,
                )
            }

            AgendaDetailAction.OnClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            AgendaDetailAction.OnConfirmClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
                viewModelScope.launch {
                    eventChannel.send(AgendaDetailEvent.OnClosed)
                }
            }

            AgendaDetailAction.OnCancelClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            else -> {
                Timber.wtf("Unknown AgendaDetailAction in VM")
            }
        }
    }
}
