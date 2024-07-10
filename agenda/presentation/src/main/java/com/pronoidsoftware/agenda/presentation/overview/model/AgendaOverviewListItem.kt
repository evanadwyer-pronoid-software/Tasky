package com.pronoidsoftware.agenda.presentation.overview.model

import com.pronoidsoftware.core.presentation.ui.now
import kotlinx.datetime.LocalDateTime

sealed class AgendaOverviewListItem(
    val id: String,
    val fromTime: LocalDateTime,
) {
    data class TimeMarker(
        val timeMarkerId: String = "TimeMarker",
        val timeMarkerNow: LocalDateTime = now(),
    ) : AgendaOverviewListItem(
        id = timeMarkerId,
        fromTime = timeMarkerNow,
    )

//    data class AgendaItem(
//        val agendaItemId: String,
//        val agendaItemFromTime: LocalDateTime
//    ) : AgendaOverviewListItem(
//        id = agendaItemId,
//        fromTime = agendaItemFromTime
//    )
}
