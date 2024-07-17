package com.pronoidsoftware.agenda.presentation.model

import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.ui.UiText
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

sealed class NotificationDuration(
    val text: UiText,
    val duration: Duration,
) {
    data object Minutes10 :
        NotificationDuration(UiText.StringResource(R.string.minutes10), 10.minutes)

    data object Minutes30 :
        NotificationDuration(UiText.StringResource(R.string.minutes30), 30.minutes)

    data object Hours1 :
        NotificationDuration(UiText.StringResource(R.string.hours1), 1.hours)

    data object Hours6 :
        NotificationDuration(UiText.StringResource(R.string.hours6), 6.hours)

    data object Days1 :
        NotificationDuration(UiText.StringResource(R.string.days1), 1.days)

    companion object {
        fun notificationDurationOptions() = listOf(
            Minutes10,
            Minutes30,
            Hours1,
            Hours6,
            Days1,
        )
    }
}
