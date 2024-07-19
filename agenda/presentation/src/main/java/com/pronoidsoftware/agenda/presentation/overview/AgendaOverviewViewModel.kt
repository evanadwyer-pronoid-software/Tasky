@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pronoidsoftware.core.domain.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import timber.log.Timber

@HiltViewModel
class AgendaOverviewViewModel @Inject constructor(
    @Named("User Initials") userInitials: String?,
    val clock: Clock,
) : ViewModel() {

    var state by mutableStateOf(
        AgendaOverviewState(
            selectedDate = today(clock),
            userInitials = userInitials
                ?: throw IllegalArgumentException(
                    "User initials not available. Has the user logged out?",
                ),
        ),
    )
        private set

    private val eventChannel = Channel<AgendaOverviewEvent>()
    val events = eventChannel.receiveAsFlow()

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
