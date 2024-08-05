package com.pronoidsoftware.agenda.network

import com.pronoidsoftware.agenda.network.mappers.toPostReminderRequest
import com.pronoidsoftware.agenda.network.mappers.toReminder
import com.pronoidsoftware.core.data.networking.AgendaRoutes
import com.pronoidsoftware.core.data.networking.delete
import com.pronoidsoftware.core.data.networking.get
import com.pronoidsoftware.core.data.networking.post
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.agendaitem.RemoteReminderDataSource
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.map
import io.ktor.client.HttpClient
import javax.inject.Inject

class KtorRemoteReminderDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : RemoteReminderDataSource {
    override suspend fun getReminder(id: String): Result<Reminder, DataError.Network> {
        return httpClient.get<ReminderDto>(
            route = AgendaRoutes.REMINDER,
            queryParameters = mapOf(
                REMINDER_ID_QUERY_PARAM to id,
            ),
        ).map { it.toReminder() }
    }

    override suspend fun postReminder(reminder: Reminder): EmptyResult<DataError.Network> {
        return httpClient.post(
            route = AgendaRoutes.REMINDER,
            body = reminder.toPostReminderRequest(),
        )
    }

    override suspend fun deleteReminder(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = AgendaRoutes.REMINDER,
            queryParameters = mapOf(
                REMINDER_ID_QUERY_PARAM to id,
            ),
        )
    }

    companion object {
        const val REMINDER_ID_QUERY_PARAM = "reminderId"
    }
}
