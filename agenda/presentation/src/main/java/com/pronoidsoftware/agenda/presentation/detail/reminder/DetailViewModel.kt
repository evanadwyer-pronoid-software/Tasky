package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.core.domain.SessionStorage
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
class DetailViewModel @Inject constructor(
    clock: Clock,
    sessionStorage: SessionStorage,
    userDataValidator: UserDataValidator,
) : ViewModel() {

    var state by mutableStateOf(
        DetailState(
            selectedDate = today(clock),
            fromTime = now(clock)
                .toInstant(TimeZone.currentSystemDefault())
                .plus(60.minutes)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
        ),
    )
        private set

    private val eventChannel = Channel<DetailEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val fullName = sessionStorage.get()!!.fullName
            state = state.copy(
                visitors = listOf(
                    VisitorUI(
                        fullName = fullName,
                        isCreator = true,
                        isGoing = true,
                    ),
                    VisitorUI(
                        fullName = fullName,
                        isCreator = false,
                        isGoing = true,
                    ),
                    VisitorUI(
                        fullName = fullName,
                        isCreator = false,
                        isGoing = false,
                    ),
                    VisitorUI(
                        fullName = fullName,
                        isCreator = false,
                        isGoing = false,
                    ),
                    VisitorUI(
                        fullName = fullName,
                        isCreator = false,
                        isGoing = false,
                    ),
                ),
            )
        }

        snapshotFlow { state.visitorToAddEmail.text }
            .onEach { email ->
                state = state.copy(
                    isVisitorToAddEmailValid = userDataValidator.validateEmail(email.toString()),
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.OnEnableEdit -> {
                state = state.copy(
                    isEditing = true,
                )
            }

            DetailAction.OnSave -> {
                viewModelScope.launch {
                    state = state.copy(
                        isEditing = false,
                    )
                    eventChannel.send(DetailEvent.OnSaved)
                }
            }

            DetailAction.OnEditTitle -> {
                state = state.copy(
                    isEditingTitle = true,
                )
            }

            DetailAction.OnCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            DetailAction.OnConfirmCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                    isEditingTitle = false,
                )
            }

            DetailAction.OnCancelCloseTitle -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            is DetailAction.OnSaveTitle -> {
                state = state.copy(
                    isEditingTitle = false,
                    title = action.newTitle,
                )
            }

            DetailAction.OnEditDescription -> {
                state = state.copy(
                    isEditingDescription = true,
                )
            }

            DetailAction.OnCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            DetailAction.OnConfirmCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                    isEditingDescription = false,
                )
            }

            DetailAction.OnCancelCloseDescription -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            is DetailAction.OnSaveDescription -> {
                state = state.copy(
                    isEditingDescription = false,
                    description = action.newDescription,
                )
            }

            is DetailAction.OnAddPhotoClick -> {
                // TODO: add compression check here
                state = state.copy(
                    photos = state.photos + action.photo,
                )
            }

            is DetailAction.OnOpenPhotoClick -> {
                state = state.copy(
                    selectedPhoto = action.photo,
//                    isViewingPhoto = true,
                )
            }

//            is DetailAction.OnEditPhotoClick -> {
//                state = state.copy(
//                    selectedPhoto = action.photo,
//                    isEditingPhoto = true
//                )
//            }

            DetailAction.OnClosePhotoClick -> {
                state = state.copy(
                    selectedPhoto = null,
//                    isViewingPhoto = false,
//                    isEditingPhoto = false,
                )
            }

            is DetailAction.OnDeletePhotoClick -> {
                state = state.copy(
                    photos = state.photos.filterNot { it == action.photo },
                    selectedPhoto = null,
//                    isViewingPhoto = false,
//                    isEditingPhoto = false,
                )
            }

            is DetailAction.OnSelectFromDate -> {
                val date = action.fromDate
                val newFromTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.fromTime.hour,
                    state.fromTime.minute,
                )
                val newToTime = if (newFromTime > state.toTime) {
                    newFromTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.toTime
                }
                state = state.copy(
                    fromTime = newFromTime,
                    toTime = newToTime,
                )
            }

            is DetailAction.OnSelectFromTime -> {
                val time = action.fromTime
                val newFromTime = LocalDateTime(
                    state.fromTime.year,
                    state.fromTime.month,
                    state.fromTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newToTime = if (newFromTime > state.toTime) {
                    newFromTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .plus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.toTime
                }
                state = state.copy(
                    fromTime = newFromTime,
                    toTime = newToTime,
                )
            }

            is DetailAction.OnSelectToDate -> {
                val date = action.toDate
                val newToTime = LocalDateTime(
                    date.year,
                    date.month,
                    date.dayOfMonth,
                    state.fromTime.hour,
                    state.fromTime.minute,
                )
                val newFromTime = if (newToTime < state.fromTime) {
                    newToTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.fromTime
                }
                state = state.copy(
                    fromTime = newFromTime,
                    toTime = newToTime,
                )
            }

            is DetailAction.OnSelectToTime -> {
                val time = action.toTime
                val newToTime = LocalDateTime(
                    state.fromTime.year,
                    state.fromTime.month,
                    state.fromTime.dayOfMonth,
                    time.hour,
                    time.minute,
                )
                val newFromTime = if (newToTime < state.fromTime) {
                    newToTime
                        .toInstant(TimeZone.currentSystemDefault())
                        .minus(30.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } else {
                    state.fromTime
                }
                state = state.copy(
                    fromTime = newFromTime,
                    toTime = newToTime,
                )
            }

            DetailAction.OnToggleFromTimePickerExpanded -> {
                state = state.copy(
                    isEditingFromTime = !state.isEditingFromTime,
                )
            }

            DetailAction.OnToggleFromDatePickerExpanded -> {
                state = state.copy(
                    isEditingFromDate = !state.isEditingFromDate,
                )
            }

            DetailAction.OnToggleToTimePickerExpanded -> {
                state = state.copy(
                    isEditingToTime = !state.isEditingToTime,
                )
            }

            DetailAction.OnToggleToDatePickerExpanded -> {
                state = state.copy(
                    isEditingToDate = !state.isEditingToDate,
                )
            }

            DetailAction.OnToggleNotificationDurationExpanded -> {
                state = state.copy(
                    isEditingNotificationDuration = !state.isEditingNotificationDuration,
                )
            }

            is DetailAction.OnSelectNotificationDuration -> {
                state = state.copy(
                    notificationDuration = action.notificationDuration,
                )
            }

            DetailAction.OnAllVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.ALL,
                )
            }

            DetailAction.OnGoingVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.GOING,
                )
            }

            DetailAction.OnNotGoingVisitorsClick -> {
                state = state.copy(
                    selectedVisitorFilter = VisitorFilterType.NOT_GOING,
                )
            }

            DetailAction.OnToggleAddVisitorDialog -> {
                state = state.copy(
                    visitorToAddEmail = TextFieldState(),
                    isShowingAddVisitorDialog = !state.isShowingAddVisitorDialog,
                )
            }

            is DetailAction.OnAddVisitorClick -> {
                viewModelScope.launch {
                    state = state.copy(
                        isAddingVisitor = true,
                    )
                    // TODO: add visitor
                    state = state.copy(
                        visitorToAddEmail = TextFieldState(),
                        isAddingVisitor = false,
                        isShowingAddVisitorDialog = false,
                    )
                }
            }

            is DetailAction.OnDeleteVisitorClick -> {
                state = state.copy(
                    visitors = state.visitors.filterNot { it == action.visitor },
                )
            }

            DetailAction.OnDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = true,
                )
            }

            DetailAction.OnConfirmDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = false,
                )
                viewModelScope.launch {
                    eventChannel.send(DetailEvent.OnDeleted)
                }
            }

            DetailAction.OnCancelDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = false,
                )
            }

            DetailAction.OnClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = true,
                )
            }

            DetailAction.OnConfirmClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
                viewModelScope.launch {
                    eventChannel.send(DetailEvent.OnClosed)
                }
            }

            DetailAction.OnCancelClose -> {
                state = state.copy(
                    isShowingCloseConfirmationDialog = false,
                )
            }

            else -> {
                Timber.wtf("Unknown ReminderDetailAction in VM")
            }
        }
    }
}
