package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.domain.AttendeeRepository
import com.pronoidsoftware.agenda.presentation.overview.mappers.toEventUi
import com.pronoidsoftware.agenda.presentation.overview.mappers.toReminderUi
import com.pronoidsoftware.agenda.presentation.overview.mappers.toTaskUi
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.auth.domain.AuthRepository
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import com.pronoidsoftware.core.domain.util.initializeAndCapitalize
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import timber.log.Timber

@HiltViewModel
class AgendaOverviewViewModel @Inject constructor(
    private val clock: Clock,
    private val sessionStorage: SessionStorage,
    private val agendaRepository: AgendaRepository,
    private val attendeeRepository: AttendeeRepository,
    private val applicationScope: CoroutineScope,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(AgendaOverviewState(selectedDate = today(clock)))
        private set

    private var agendaForTodayJob: Job? = null

    private val eventChannel = Channel<AgendaOverviewEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        agendaForTodayJob = getAgendaForTodayFlow()

        viewModelScope.launch {
            state = state.copy(
                userInitials = sessionStorage.get()?.fullName?.initializeAndCapitalize()
                    ?: error("User initials not available. Has the user logged out?"),
                isLoading = true,
            )
            agendaRepository.syncPendingReminders()
            agendaRepository.syncPendingTasks()
            agendaRepository.fetchAllAgendaItems()
            state = state.copy(
                isLoading = false,
            )
        }
    }

    fun onAction(action: AgendaOverviewAction) {
        when (action) {
            is AgendaOverviewAction.OnSelectDate -> {
                state = state.copy(
                    selectedDate = action.date,
                )
                agendaForTodayJob = getAgendaForTodayFlow()
            }

            AgendaOverviewAction.OnToggleProfileDropdownMenu -> {
                state = state.copy(
                    profileDropdownMenuExpanded = !state.profileDropdownMenuExpanded,
                )
            }

            AgendaOverviewAction.OnToggleDatePickerExpanded -> {
                state = state.copy(
                    datePickerExpanded = !state.datePickerExpanded,
                )
            }

            AgendaOverviewAction.OnToggleFABDropdownMenuExpanded -> {
                state = state.copy(
                    fabDropdownMenuExpanded = !state.fabDropdownMenuExpanded,
                )
            }

            is AgendaOverviewAction.OnTickClick -> {
                viewModelScope.launch {
                    agendaRepository.getTask(action.id)?.let { task ->
                        agendaRepository.updateTask(
                            task = task.copy(
                                isCompleted = !task.isCompleted,
                            ),
                        )
                    }
                }
            }

            is AgendaOverviewAction.OnDeleteClick -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = true,
                    agendaTypeToDelete = action.type,
                    agendaItemIdToDelete = action.id,
                    eventToDeleteHostId = action.eventHostId,
                )
            }

            AgendaOverviewAction.OnConfirmDelete -> {
                viewModelScope.launch {
                    val localUserId = sessionStorage.get()?.userId ?: return@launch
                    when (state.agendaTypeToDelete) {
                        AgendaItemType.EVENT -> {
                            if (state.eventToDeleteHostId == localUserId) {
                                agendaRepository.deleteEvent(
                                    state.agendaItemIdToDelete,
                                )
                            } else {
                                agendaRepository.removeAttendee(
                                    id = state.agendaItemIdToDelete,
                                )
                                attendeeRepository.removeAttendeeFromEvent(
                                    eventId = state.agendaItemIdToDelete,
                                )
                            }
                        }
                        AgendaItemType.TASK -> agendaRepository.deleteTask(
                            state.agendaItemIdToDelete,
                        )
                        AgendaItemType.REMINDER -> agendaRepository.deleteReminder(
                            state.agendaItemIdToDelete,
                        )
                        null -> Unit
                    }
                    state = state.copy(
                        isShowingDeleteConfirmationDialog = false,
                        agendaTypeToDelete = null,
                        agendaItemIdToDelete = "",
                        eventToDeleteHostId = null,
                    )
                    eventChannel.send(
                        AgendaOverviewEvent.OnDelete,
                    )
                }
            }

            AgendaOverviewAction.OnCancelDelete -> {
                state = state.copy(
                    isShowingDeleteConfirmationDialog = false,
                    agendaTypeToDelete = null,
                    agendaItemIdToDelete = "",
                    eventToDeleteHostId = null,
                )
            }

            AgendaOverviewAction.OnLogoutClick -> {
                logout()
            }

            else -> {
                Timber.wtf("Unknown AgendaOverviewAction in VM")
            }
        }
    }

    private fun getAgendaForTodayFlow(): Job {
        agendaForTodayJob?.cancel()
        return agendaRepository.getAgendaItemsForDate(state.selectedDate).onEach { agendaItems ->
            val timedItems = if (state.selectedDate == today(clock)) {
                val temp = mutableListOf<AgendaItem?>()
                var addedTimeMarker = false
                agendaItems.forEach {
                    if (it.startDateTime > now(clock) && !addedTimeMarker) {
                        temp.add(null)
                        addedTimeMarker = true
                    }
                    temp.add(it)
                }
                if (!addedTimeMarker && temp.isNotEmpty()) {
                    temp.add(null)
                }
                temp
            } else {
                agendaItems
            }
            val items = timedItems.map { agendaItem ->
                when (agendaItem) {
                    is AgendaItem.Event -> agendaItem.toEventUi()
                    is AgendaItem.Reminder -> agendaItem.toReminderUi()
                    is AgendaItem.Task -> agendaItem.toTaskUi()
                    null -> AgendaOverviewItemUi.TimeMarker
                }
            }
            state = state.copy(items = items)
        }.launchIn(viewModelScope)
    }

    private fun logout() {
        applicationScope.launch {
            state = state.copy(
                isLoading = true,
            )
            agendaForTodayJob?.cancel()
            agendaRepository.deleteAllAgendaItems()
            authRepository.logout()
            sessionStorage.set(null)
            eventChannel.send(AgendaOverviewEvent.OnLogout)
        }
    }
}
