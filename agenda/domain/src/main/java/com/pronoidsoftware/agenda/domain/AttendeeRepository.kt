package com.pronoidsoftware.agenda.domain

import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result

interface AttendeeRepository {
    suspend fun getAttendee(email: String): Result<Attendee?, DataError.Network>
    suspend fun removeAttendeeFromEvent(eventId: EventId)
}
