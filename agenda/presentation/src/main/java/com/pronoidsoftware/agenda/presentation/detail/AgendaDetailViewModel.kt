package com.pronoidsoftware.agenda.presentation.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.minus
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.plus
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.domain.validation.UserDataValidator
import com.pronoidsoftware.core.presentation.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import timber.log.Timber

@HiltViewModel
class AgendaDetailViewModel @Inject constructor(
    clock: Clock,
    userDataValidator: UserDataValidator,
    savedStateHandle: SavedStateHandle,
    private val sessionStorage: SessionStorage,
    private val agendaRepository: AgendaRepository,
) : ViewModel() {

    private fun SavedStateHandle.isEditing(): Boolean {
        return get<Boolean>("isEditing") ?: false
    }

    private fun SavedStateHandle.getAgendaItemType(): AgendaItemType? {
        return get<String>("type")
            ?.let { AgendaItemType.valueOf(it) }
    }

    private fun SavedStateHandle.getId(): String? {
        return get<String>("id")
    }

    var state by mutableStateOf(
        AgendaDetailState(
            agendaItemId = savedStateHandle.getId(),
            selectedDate = today(clock),
            startDateTime = now(clock).plus(60.minutes),
            isEditing = savedStateHandle.isEditing(),
            agendaItemType = savedStateHandle.getAgendaItemType(),
            typeSpecificDetails = when (savedStateHandle.getAgendaItemType()) {
                AgendaItemType.EVENT -> AgendaItemDetails.Event()
                AgendaItemType.TASK -> AgendaItemDetails.Task()
                AgendaItemType.REMINDER -> AgendaItemDetails.Reminder
                null -> null
            },
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

        savedStateHandle.getId()?.let { agendaItemId ->
            viewModelScope.launch {
                state = state.copy(
                    isLoading = true,
                )
                when (savedStateHandle.getAgendaItemType()) {
                    AgendaItemType.EVENT -> {
                        agendaRepository.getEvent(agendaItemId)?.let { event ->
                            state = state.copy(
                                title = event.title,
                                description = event.description,
                                startDateTime = event.startDateTime,
                                notificationDuration = calculateNotificationDuration(
                                    startDateTime = event.startDateTime,
                                    notificationDateTime = event.notificationDateTime,
                                ),
                                typeSpecificDetails = AgendaItemDetails.Event(
                                    host = event.host,
                                    isUserEventCreator = event.isUserEventCreator,
                                    isLocalUserGoing = event.isLocalUserGoing,
                                    photos = event.photos,
                                    endDateTime = event.endDateTime,
                                    attendees = event.attendees,
                                ),
                            )
                        }
                    }

                    AgendaItemType.TASK -> {
                        agendaRepository.getTask(agendaItemId)?.let { task ->
                            state = state.copy(
                                title = task.title,
                                description = task.description,
                                startDateTime = task.startDateTime,
                                notificationDuration = calculateNotificationDuration(
                                    startDateTime = task.startDateTime,
                                    notificationDateTime = task.notificationDateTime,
                                ),
                                typeSpecificDetails = AgendaItemDetails.Task(
                                    completed = task.isCompleted,
                                ),
                            )
                        }
                    }

                    AgendaItemType.REMINDER -> {
                        agendaRepository.getReminder(agendaItemId)?.let { reminder ->
                            state = state.copy(
                                title = reminder.title,
                                description = reminder.description,
                                startDateTime = reminder.startDateTime,
                                notificationDuration = calculateNotificationDuration(
                                    startDateTime = reminder.startDateTime,
                                    notificationDateTime = reminder.notificationDateTime,
                                ),
                            )
                        }
                    }

                    null -> Unit
                }
                state = state.copy(
                    isLoading = false,
                )
            }
        }
    }

    private fun calculateNotificationDuration(
        startDateTime: LocalDateTime,
        notificationDateTime: LocalDateTime,
    ): NotificationDuration {
        val startInstant = startDateTime.toInstant(TimeZone.currentSystemDefault())
        val notificationInstant = notificationDateTime.toInstant(TimeZone.currentSystemDefault())
        val difference = (startInstant - notificationInstant).toInt(DurationUnit.MINUTES)
        return when (difference) {
            10 -> NotificationDuration.Minutes10
            30 -> NotificationDuration.Minutes30
            60 -> NotificationDuration.Hours1
            6 * 60 -> NotificationDuration.Hours6
            24 * 60 -> NotificationDuration.Days1
            else -> NotificationDuration.Minutes30
        }
    }

    private fun getDetailsAsEvent(): AgendaItemDetails.Event? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Event)
    }

    private fun getDetailsAsTask(): AgendaItemDetails.Task? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Task)
    }

    private fun getDetailsAsReminder(): AgendaItemDetails.Reminder? {
        return (state.typeSpecificDetails as? AgendaItemDetails.Reminder)
    }

    fun onAction(action: AgendaDetailAction) {
        when (action) {
            AgendaDetailAction.OnEnableEdit -> {
                state = state.copy(
                    isEditing = true,
                )
            }

            AgendaDetailAction.OnSave -> {
                viewModelScope.launch {
                    state = state.copy(
                        isEditing = false,
                        isSaving = true,
                    )
                    val isCreateAgendaItem = state.agendaItemId == null
                    val localUserId = sessionStorage.get()?.userId ?: return@launch
                    when (state.agendaItemType) {
                        AgendaItemType.EVENT -> {
                            val eventDetails = state.typeSpecificDetails as AgendaItemDetails.Event
                            val event = AgendaItem.Event(
                                id = state.agendaItemId ?: UUID.randomUUID().toString(),
                                title = state.title,
                                description = state.description,
                                startDateTime = state.startDateTime,
                                endDateTime = eventDetails.endDateTime,
                                notificationDateTime = state.startDateTime
                                    .minus(state.notificationDuration.duration),
                                host = if (isCreateAgendaItem) {
                                    localUserId
                                } else {
                                    eventDetails.host
                                },
                                isUserEventCreator = if (isCreateAgendaItem) {
                                    true
                                } else {
                                    eventDetails.isUserEventCreator
                                },
                                attendees = eventDetails.attendees,
                                photos = eventDetails.photos,
                                deletedPhotos = if (isCreateAgendaItem) {
                                    emptyList()
                                } else {
                                    eventDetails.deletedPhotos
                                },
                                isLocalUserGoing = if (isCreateAgendaItem) {
                                    true
                                } else {
                                    eventDetails.isLocalUserGoing
                                },
                            )
                            val result = if (isCreateAgendaItem) {
                                agendaRepository.createEventLocallyEnqueueRemote(event)
                            } else {
                                agendaRepository.updateEvent(event)
                            }
                            when (result) {
                                is Result.Error -> {
                                    eventChannel.send(
                                        AgendaDetailEvent.OnError(result.error.asUiText()),
                                    )
                                }

                                is Result.Success -> {
                                    state = state.copy(
                                        typeSpecificDetails = eventDetails.copy(
                                            deletedPhotos = emptyList(),
                                            uploadWorkId = UUID.fromString(result.data),
                                        ),
                                    )
                                    eventChannel.send(AgendaDetailEvent.OnSaved)
                                }
                            }
                        }

                        AgendaItemType.TASK -> {
                            val task = AgendaItem.Task(
                                id = state.agendaItemId ?: UUID.randomUUID().toString(),
                                title = state.title,
                                description = state.description,
                                startDateTime = state.startDateTime,
                                notificationDateTime = state.startDateTime
                                    .minus(state.notificationDuration.duration),
                                isCompleted = false,
                            )
                            val result = if (isCreateAgendaItem) {
                                agendaRepository.createTask(task)
                            } else {
                                agendaRepository.updateTask(task)
                            }
                            when (result) {
                                is Result.Error -> {
                                    eventChannel.send(
                                        AgendaDetailEvent.OnError(result.error.asUiText()),
                                    )
                                }

                                is Result.Success -> {
                                    eventChannel.send(AgendaDetailEvent.OnSaved)
                                }
                            }
                        }

                        AgendaItemType.REMINDER -> {
                            val reminder = AgendaItem.Reminder(
                                id = state.agendaItemId ?: UUID.randomUUID().toString(),
                                title = state.title,
                                description = state.description,
                                startDateTime = state.startDateTime,
                                notificationDateTime = state.startDateTime
                                    .minus(state.notificationDuration.duration),
                            )
                            val result = if (isCreateAgendaItem) {
                                agendaRepository.createReminder(reminder)
                            } else {
                                agendaRepository.updateReminder(reminder)
                            }
                            when (result) {
                                is Result.Error -> {
                                    eventChannel.send(
                                        AgendaDetailEvent.OnError(result.error.asUiText()),
                                    )
                                }

                                is Result.Success -> {
                                    eventChannel.send(AgendaDetailEvent.OnSaved)
                                }
                            }
                        }

                        null -> {
                        }
                    }

                    state = state.copy(isSaving = false)
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
                            deletedPhotos = if (action.photo is Photo.Remote) {
                                eventDetails.deletedPhotos + action.photo
                            } else {
                                eventDetails.deletedPhotos
                            },
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
                        newStartDateTime.plus(30.minutes)
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
                        newStartDateTime.plus(30.minutes)
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
                        newEndDateTime.minus(30.minutes)
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
                        newEndDateTime.minus(30.minutes)
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

            is AgendaDetailAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(
                    showNotificationRationale = action.showNotificationRationale,
                )
            }

            AgendaDetailAction.DismissNotificationRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
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
                            attendees = eventDetails.attendees.filterNot {
                                it.userId == action.visitor.userId
                            },
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
