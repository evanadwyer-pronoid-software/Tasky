package com.pronoidsoftware.agenda.network

import com.pronoidsoftware.agenda.domain.AttendeeRepository
import com.pronoidsoftware.agenda.network.dto.GetAttendeeDto
import com.pronoidsoftware.agenda.network.mappers.toAttendee
import com.pronoidsoftware.core.data.networking.AttendeeRoutes
import com.pronoidsoftware.core.data.networking.get
import com.pronoidsoftware.core.domain.agendaitem.Attendee
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.map
import io.ktor.client.HttpClient
import javax.inject.Inject

class AttendeeRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : AttendeeRepository {
    override suspend fun getAttendee(email: String): Result<Attendee?, DataError.Network> {
        return httpClient.get<GetAttendeeDto>(
            route = AttendeeRoutes.ATTENDEE,
            queryParameters = mapOf(
                EMAIL to email,
            ),
        ).map { it.toAttendee() }
    }

    override suspend fun removeAttendeeFromEvent(eventId: EventId) {
        TODO("Not yet implemented")
    }

    companion object {
        const val EMAIL = "email"
    }
}
