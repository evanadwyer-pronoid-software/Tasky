package com.pronoidsoftware.agenda.presentation.overview.model

sealed class AgendaOverviewItemUi(
    val id: String,
) {
    data object TimeMarker : AgendaOverviewItemUi(id = "TimeMarker")

    data class Item(
        val item: AgendaOverviewItemContents,
    ) : AgendaOverviewItemUi(id = item.id)
}

sealed class AgendaOverviewItemContents(
    open val id: String,
    open val title: String,
    open val description: String,
    open val startDateTime: String,
) {
    data class ReminderOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val startDateTime: String,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
    )

    data class TaskOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val startDateTime: String,
        val completed: Boolean,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
    )

    data class EventOverviewUiContents(
        override val id: String,
        override val title: String,
        override val description: String,
        override val startDateTime: String,
        val endDateTime: String,
    ) : AgendaOverviewItemContents(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime,
    )
}
