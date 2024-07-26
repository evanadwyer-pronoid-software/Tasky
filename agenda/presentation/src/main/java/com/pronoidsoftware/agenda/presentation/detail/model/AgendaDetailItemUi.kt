package com.pronoidsoftware.agenda.presentation.detail.model

import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI

// @Serializable
sealed class AgendaDetailItemUi(
    open val id: String,
    open val title: String,
    open val description: String,
    open val fromTime: String,
    open val fromDate: String,
    open val notificationDuration: String,
) {
    data class ReminderOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        override val fromDate: String,
        override val notificationDuration: String,
    ) : AgendaDetailItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
        fromDate = fromDate,
        notificationDuration = notificationDuration,
    )

    data class TaskOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        override val fromDate: String,
        override val notificationDuration: String,
        val completed: Boolean,
    ) : AgendaDetailItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
        fromDate = fromDate,
        notificationDuration = notificationDuration,
    )

    data class EventOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        val photos: List<PhotoId>,
        override val fromTime: String,
        override val fromDate: String,
        val toTime: String,
        val toDate: String,
        override val notificationDuration: String,
        val visitors: List<VisitorUI>,
    ) : AgendaDetailItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
        fromDate = fromDate,
        notificationDuration = notificationDuration,
    )
}

// fun newReminder(): AgendaDetailItemUi.ReminderOverviewUi {
//    return AgendaDetailItemUi.ReminderOverviewUi(
//        id = UUID.randomUUID().toString(),
//        title = "New Reminder",
//        description = "",
//        fromTime = now().time.formatHours().asString(),
//        fromDate = now().date.formatRelativeDate().asString(),
//        notificationDuration = NotificationDuration.Minutes30.text.asString(),
//    )
// }
//
// fun newReminder(): AgendaDetailItemUi.ReminderOverviewUi {
//    return AgendaDetailItemUi.ReminderOverviewUi(
//        id = "",
//        title = "",
//        description = "",
//        fromTime = "",
//        notificationDuration = NotificationDuration.Minutes30,
//    )
// }
//
// fun newReminder(): AgendaDetailItemUi.ReminderOverviewUi {
//    return AgendaDetailItemUi.ReminderOverviewUi(
//        id = "",
//        title = "",
//        description = "",
//        fromTime = "",
//        notificationDuration = NotificationDuration.Minutes30,
//    )
// }
