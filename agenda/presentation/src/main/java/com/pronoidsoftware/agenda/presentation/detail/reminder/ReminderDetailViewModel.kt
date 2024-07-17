package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import timber.log.Timber

@HiltViewModel
class ReminderDetailViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(
        ReminderDetailState(
            title = "Project X",
            description = "Weekly plan\nRole distribution",
        ),
    )
        private set

    private val eventChannel = Channel<ReminderDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ReminderDetailAction) {
        when (action) {
            ReminderDetailAction.OnEnableEdit -> {
                state = state.copy(
                    isEditing = true,
                )
            }

            ReminderDetailAction.OnSave -> {
                viewModelScope.launch {
                    state = state.copy(
                        isEditing = false,
                    )
                    eventChannel.send(ReminderDetailEvent.OnSaved)
                }
            }

            is ReminderDetailAction.OnSelectDate -> {
                val date = action.date
                state = state.copy(
                    fromTime = LocalDateTime(
                        date.year,
                        date.month,
                        date.dayOfMonth,
                        state.fromTime.hour,
                        state.fromTime.minute,
                    ),
                )
            }

            is ReminderDetailAction.OnSelectTime -> {
                val time = action.time
                state = state.copy(
                    fromTime = LocalDateTime(
                        state.fromTime.year,
                        state.fromTime.month,
                        state.fromTime.dayOfMonth,
                        time.hour,
                        time.minute,
                    ),
                )
            }

            is ReminderDetailAction.OnSelectNotificationDuration -> {
                state = state.copy(
                    notificationDuration = action.notificationDuration,
                )
            }

            is ReminderDetailAction.OnDelete -> {
                viewModelScope.launch {
                    eventChannel.send(ReminderDetailEvent.OnDeleted)
                }
            }

            else -> {
                Timber.wtf("Unknown ReminderDetailAction in VM")
            }
        }
    }
}
