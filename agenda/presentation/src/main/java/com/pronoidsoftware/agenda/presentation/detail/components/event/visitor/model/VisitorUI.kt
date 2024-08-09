package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model

import com.pronoidsoftware.core.domain.agendaitem.Attendee

data class VisitorUI(
    val id: String = "",
    val fullName: String = "",
    val isCreator: Boolean = false,
    val isGoing: Boolean = false,
)

fun Attendee.toVisitorUi(isEventCreator: Boolean): VisitorUI {
    return VisitorUI(
        id = userId,
        fullName = fullName,
        isGoing = isGoing,
        isCreator = isEventCreator,
    )
}
