package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.initializeAndCapitalize
import com.pronoidsoftware.core.domain.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import timber.log.Timber

@HiltViewModel
class AgendaOverviewViewModel @Inject constructor(
    sessionStorage: SessionStorage,
    clock: Clock,
) : ViewModel() {

    var state by mutableStateOf(AgendaOverviewState(selectedDate = today(clock)))
        private set

    private val eventChannel = Channel<AgendaOverviewEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = state.copy(
                userInitials = sessionStorage.get()?.fullName?.initializeAndCapitalize()
                    ?: error("User initials not available. Has the user logged out?"),
            )
        }
    }

    fun onAction(action: AgendaOverviewAction) {
        when (action) {
            is AgendaOverviewAction.OnSelectDate -> {
                state = state.copy(
                    selectedDate = action.date,
                )
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

            else -> {
                Timber.wtf("Unknown AgendaOverviewAction in VM")
            }
        }
    }
}
