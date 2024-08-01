package com.pronoidsoftware.agenda.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
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
        snapshotFlow { getDetailsAsEvent()?.visitorToAddEmail?.text }
            .onEach { email ->
                state = state.copy(
                    typeSpecificDetails = getDetailsAsEvent()?.copy(
                        isVisitorToAddEmailValid = userDataValidator.validateEmail(
                            email.toString(),
                        ),
                    ),
                )
            }
            .launchIn(viewModelScope)
    }

    fun getDetailsAsEvent(): AgendaItemDetails.Event? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Event)
    }

    fun getDetailsAsTask(): AgendaItemDetails.Task? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Task)
    }

    fun getDetailsAsReminder(): AgendaItemDetails.Reminder? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Reminder)
    }

    fun onAction(action: AgendaDetailAction) {
        when (action) {
            is AgendaDetailAction.OnSetAgendaItemType -> {
                state = state.copy(
                    agendaItemType = action.type,
                    typeSpecificDetails = when (action.type) {
                        AgendaItemType.EVENT -> AgendaItemDetails.Event()
                        AgendaItemType.TASK -> AgendaItemDetails.Task()
                        AgendaItemType.REMINDER -> AgendaItemDetails.Reminder
                    },
                )
            }

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
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            photos = eventDetails.photos + action.photo,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnOpenPhotoClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            selectedPhotoToView = action.photo,
                        ),
                    )
                }
            }

            AgendaDetailAction.OnClosePhotoClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            selectedPhotoToView = null,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnDeletePhotoClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            photos = eventDetails.photos.filterNot { it == action.photo },
                            selectedPhotoToView = null,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnSelectStartDate -> {
                val date = action.startDate
                val newStartDateTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.startDateTime.hour,
                    state.startDateTime.minute,
                )
                state = state.copy(
                    startDateTime = newStartDateTime,
                )
                getDetailsAsEvent()?.let { eventDetails ->
                    val newEndDateTime = if (newStartDateTime > eventDetails.endDateTime) {
                        newStartDateTime
                            .toInstant(TimeZone.currentSystemDefault())
                            .plus(30.minutes)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                    } else {
                        eventDetails.endDateTime
                    }
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            endDateTime = newEndDateTime,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnSelectStartTime -> {
                val time = action.startTime
                val newStartDateTime = LocalDateTime(
                    state.startDateTime.year,
                    state.startDateTime.month,
                    state.startDateTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                state = state.copy(
                    startDateTime = newStartDateTime,
                )
                getDetailsAsEvent()?.let { eventDetails ->
                    val newEndDateTime = if (newStartDateTime > eventDetails.endDateTime) {
                        newStartDateTime
                            .toInstant(TimeZone.currentSystemDefault())
                            .plus(30.minutes)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                    } else {
                        eventDetails.endDateTime
                    }
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            endDateTime = newEndDateTime,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnSelectEndDate -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    val date = action.endDate
                    val newEndDateTime = LocalDateTime(
                        date.year,
                        date.month,
                        date.dayOfMonth,
                        eventDetails.endDateTime.hour,
                        eventDetails.endDateTime.minute,
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
                        typeSpecificDetails = eventDetails.copy(
                            endDateTime = newEndDateTime,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnSelectEndTime -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    val time = action.endTime
                    val newEndDateTime = LocalDateTime(
                        eventDetails.endDateTime.year,
                        eventDetails.endDateTime.month,
                        eventDetails.endDateTime.dayOfMonth,
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
                        typeSpecificDetails = eventDetails.copy(
                            endDateTime = newEndDateTime,
                        ),
                    )
                }
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
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            isEditingEndTime = !eventDetails.isEditingEndTime,
                        ),
                    )
                }
            }

            AgendaDetailAction.OnToggleEndDatePickerExpanded -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            isEditingEndDate = !eventDetails.isEditingEndDate,
                        ),
                    )
                }
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
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            selectedVisitorFilter = VisitorFilterType.ALL,
                        ),
                    )
                }
            }

            AgendaDetailAction.OnGoingVisitorsClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            selectedVisitorFilter = VisitorFilterType.GOING,
                        ),
                    )
                }
            }

            AgendaDetailAction.OnNotGoingVisitorsClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            selectedVisitorFilter = VisitorFilterType.NOT_GOING,
                        ),
                    )
                }
            }

            AgendaDetailAction.OnToggleAddVisitorDialog -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            visitorToAddEmail = TextFieldState(),
                            isShowingAddVisitorDialog = !eventDetails.isShowingAddVisitorDialog,
                        ),
                    )
                }
            }

            is AgendaDetailAction.OnAddVisitorClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    viewModelScope.launch {
                        state = state.copy(
                            typeSpecificDetails = eventDetails.copy(
                                isAddingVisitor = true,
                            ),
                        )
                        // TODO: add visitor, update if error
                        state = state.copy(
                            typeSpecificDetails = eventDetails.copy(
                                visitorToAddEmail = TextFieldState(),
                                isAddingVisitor = false,
                                isShowingAddVisitorDialog = false,
                            ),
                        )
                    }
                }
            }

            is AgendaDetailAction.OnDeleteVisitorClick -> {
                getDetailsAsEvent()?.let { eventDetails ->
                    state = state.copy(
                        typeSpecificDetails = eventDetails.copy(
                            visitors = eventDetails.visitors.filterNot { it == action.visitor },
                        ),
                    )
                }
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
