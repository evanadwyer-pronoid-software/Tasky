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
            fromDateTime = now(clock)
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

            is AgendaDetailAction.OnSelectFromDate -> {
                val date = action.fromDate
                val newFromTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.fromDateTime.hour,
                    state.fromDateTime.minute,
                )
                val newToTime = if (newFromTime > state.toDateTime) {
                    newFromTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.toDateTime
                }
                state = state.copy(
                    fromDateTime = newFromTime,
                    toDateTime = newToTime,
                )
            }

            is AgendaDetailAction.OnSelectFromTime -> {
                val time = action.fromTime
                val newFromTime = LocalDateTime(
                    state.fromDateTime.year,
                    state.fromDateTime.month,
                    state.fromDateTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newToTime = if (newFromTime > state.toDateTime) {
                    newFromTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.toDateTime
                }
                state = state.copy(
                    fromDateTime = newFromTime,
                    toDateTime = newToTime,
                )
            }

            is AgendaDetailAction.OnSelectToDate -> {
                val date = action.toDate
                val newToTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.fromDateTime.hour,
                    state.fromDateTime.minute,
                )
                val newFromTime = if (newToTime < state.fromDateTime) {
                    newToTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.fromDateTime
                }
                state = state.copy(
                    fromDateTime = newFromTime,
                    toDateTime = newToTime,
                )
            }

            is AgendaDetailAction.OnSelectToTime -> {
                val time = action.toTime
                val newToTime = LocalDateTime(
                    state.fromDateTime.year,
                    state.fromDateTime.month,
                    state.fromDateTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newFromTime = if (newToTime < state.fromDateTime) {
                    newToTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.fromDateTime
                }
                state = state.copy(
                    fromDateTime = newFromTime,
                    toDateTime = newToTime,
                )
            }

            AgendaDetailAction.OnToggleFromTimePickerExpanded -> {
                state = state.copy(
                    isEditingFromTime = !state.isEditingFromTime,
                )
            }

            AgendaDetailAction.OnToggleFromDatePickerExpanded -> {
                state = state.copy(
                    isEditingFromDate = !state.isEditingFromDate,
                )
            }

            AgendaDetailAction.OnToggleToTimePickerExpanded -> {
                state = state.copy(
                    isEditingToTime = !state.isEditingToTime,
                )
            }

            AgendaDetailAction.OnToggleToDatePickerExpanded -> {
                state = state.copy(
                    isEditingToDate = !state.isEditingToDate,
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
