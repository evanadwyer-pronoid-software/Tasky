package com.pronoidsoftware.agenda.presentation.overview.model

sealed class AgendaOverviewItemUi(
    open val id: String,
) {
    data class TimeMarker(
        override val id: String = "TimeMarker",
    ) : AgendaOverviewItemUi(id = id)

    data class Item(
        val item: AgendaOverviewItemContents,
        override val id: String,
    ) : AgendaOverviewItemUi(id = id)
}

sealed class AgendaOverviewItemContents(
    open val id: String,
    open val title: String,
    open val description: String,
    open val fromTime: String,
) {
    data class ReminderOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )

    data class TaskOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        val completed: Boolean,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )

    data class EventOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        val toTime: String,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )
}
