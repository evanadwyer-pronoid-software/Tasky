package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewListItem
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agenda.AgendaItem
import com.pronoidsoftware.core.domain.util.toInitials
import com.pronoidsoftware.core.presentation.ui.capitalizeInitials
import com.pronoidsoftware.core.presentation.ui.now
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AgendaOverviewViewModel @Inject constructor(
    sessionStorage: SessionStorage,
) : ViewModel() {

    var state by mutableStateOf(AgendaOverviewState())
        private set

    private val eventChannel = Channel<AgendaOverviewEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = AgendaOverviewState(
                userInitials = sessionStorage.get()?.fullName?.toInitials()?.capitalizeInitials()
                    ?: "",
                items = listOf(
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "1",
                        type = AgendaItem.TASK,
                        title = "Project X",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = null,
                        completed = false,
                    ),
                    AgendaOverviewListItem.TimeMarker(),
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "2",
                        type = AgendaItem.EVENT,
                        title = "Project Y",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = "Jul 2, 12:05",
                        completed = false,
                    ),
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "3",
                        type = AgendaItem.REMINDER,
                        title = "Project Z",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = null,
                        completed = false,
                    ),
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "4",
                        type = AgendaItem.REMINDER,
                        title = "Project Z",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = null,
                        completed = false,
                    ),
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "5",
                        type = AgendaItem.REMINDER,
                        title = "Project Z",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = null,
                        completed = false,
                    ),
                    AgendaOverviewItemUi(
                        agendaOverviewItemUiId = "6",
                        type = AgendaItem.REMINDER,
                        title = "Project Z",
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        agendaOverviewItemUiFromTime = now(),
                        toTime = null,
                        completed = false,
                    ),
                ),
            )
        }
    }

    fun onAction(action: AgendaOverviewAction) {
        when (action) {
            is AgendaOverviewAction.OnTickClick -> {
                val item = state.items.find { it.id == action.id }
                val items = state.items.filter { it.id != action.id }
                item?.let { itemToBeUpdated ->
                    if (itemToBeUpdated is AgendaOverviewItemUi) {
                        state = state.copy(
                            items = items +
                                itemToBeUpdated.copy(
                                    completed = !itemToBeUpdated.completed,
                                ),
                        )
                    }
                }
            }

            is AgendaOverviewAction.OnDeleteClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaOverviewEvent.Delete(action.id))
                }
            }

            is AgendaOverviewAction.OnEditClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaOverviewEvent.Edit(action.id))
                }
            }

            is AgendaOverviewAction.OnOpenClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaOverviewEvent.Open(action.id))
                }
            }

            is AgendaOverviewAction.OnCreateClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaOverviewEvent.Create(action.type))
                }
            }

            is AgendaOverviewAction.OnSelectDate -> {
                state = state.copy(
                    selectedDate = action.date,
                )
            }

            is AgendaOverviewAction.OnLogoutClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaOverviewEvent.Logout)
                }
            }

            else -> {
                Timber.wtf("Unknown AgendaOverview Action in VM")
            }
        }
    }
}
