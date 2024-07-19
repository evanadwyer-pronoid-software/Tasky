package com.pronoidsoftware.agenda.presentation.detail.reminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

@HiltViewModel
class ReminderDetailViewModel @Inject constructor(
    val clock: Clock,
) : ViewModel() {

    var state by mutableStateOf(
        ReminderDetailState(
            selectedDate = today(clock),
            atTime = now(clock)
                .toInstant(TimeZone.currentSystemDefault())
                .plus(60.minutes)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
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
                    atTime = LocalDateTime(
                        date.year,
                        date.month,
                        date.dayOfMonth,
                        state.atTime.hour,
                        state.atTime.minute,
                    ),
                )
            }

            is ReminderDetailAction.OnSelectTime -> {
                val time = action.time
                state = state.copy(
                    atTime = LocalDateTime(
                        state.atTime.year,
                        state.atTime.month,
                        state.atTime.dayOfMonth,
                        time.hour,
                        time.minute,
                    ),
                )
            }

            ReminderDetailAction.OnToggleTimePickerExpanded -> {
                state = state.copy(
                    isEditingTime = !state.isEditingTime,
                )
            }

            ReminderDetailAction.OnToggleDatePickerExpanded -> {
                state = state.copy(
                    isEditingDate = !state.isEditingDate,
                )
            }

            ReminderDetailAction.OnToggleNotificationDurationExpanded -> {
                state = state.copy(
                    isEditingNotificationDuration = !state.isEditingNotificationDuration,
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
