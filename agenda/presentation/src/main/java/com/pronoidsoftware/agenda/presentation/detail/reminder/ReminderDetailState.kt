package com.pronoidsoftware.agenda.presentation.detail.reminder

import com.pronoidsoftware.agenda.presentation.detail.model.NotificationDuration
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.today
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class ReminderDetailState(
    val clock: Clock = Clock.System,
    val selectedDate: LocalDate = today(clock),
    val title: String = "",
    val description: String? = null,
    val atTime: LocalDateTime = now(clock)
        .toInstant(TimeZone.currentSystemDefault())
        .plus(60.minutes)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val notificationDuration: NotificationDuration = NotificationDuration.Minutes30,
    val isEditing: Boolean = false,
    val isEditingTitle: Boolean = false,
    val isEditingDescription: Boolean = false,
    val isEditingTime: Boolean = false,
    val isEditingDate: Boolean = false,
    val isEditingNotificationDuration: Boolean = false,
    val isShowingDeleteConfirmationDialog: Boolean = false,
    val isShowingCloseConfirmationDialog: Boolean = false,
)
