package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.toInitials
import com.pronoidsoftware.core.presentation.ui.capitalizeInitials
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

    var state by mutableStateOf(
        AgendaOverviewState(
            clock = clock,
        ),
    )
        private set

    private val eventChannel = Channel<AgendaOverviewEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            state = AgendaOverviewState(
                // TODO: provide user initials another way to avoid initializing state twice?
                userInitials = sessionStorage.get()?.fullName?.toInitials()?.capitalizeInitials()
                    ?: "",
                clock = clock,
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

            else -> {
                Timber.wtf("Unknown AgendaOverviewAction in VM")
            }
        }
    }
}
