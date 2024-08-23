package com.pronoidsoftware.agenda.network.mappers

import com.pronoidsoftware.agenda.network.dto.GetAttendeeDto
import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.util.now

fun GetAttendeeDto.toAttendee(): Attendee? {
    return if (doesUserExist) {
        attendee?.let {
            Attendee(
                userId = it.userId,
                fullName = it.fullName,
                isGoing = true,
                remindAt = now(),
            )
        }
    } else {
        null
    }
}
