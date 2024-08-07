package com.pronoidsoftware.agenda.network

import com.pronoidsoftware.agenda.network.dto.AgendaDto
import com.pronoidsoftware.agenda.network.dto.ReminderDto
import com.pronoidsoftware.agenda.network.dto.TaskDto
import com.pronoidsoftware.agenda.network.mappers.toReminder
import com.pronoidsoftware.agenda.network.mappers.toTask
import com.pronoidsoftware.agenda.network.mappers.toUpsertReminderRequest
import com.pronoidsoftware.agenda.network.mappers.toUpsertTaskRequest
import com.pronoidsoftware.core.data.networking.AgendaRoutes
import com.pronoidsoftware.core.data.networking.delete
import com.pronoidsoftware.core.data.networking.get
import com.pronoidsoftware.core.data.networking.post
import com.pronoidsoftware.core.data.networking.put
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.map
import io.ktor.client.HttpClient
import javax.inject.Inject

class KtorRemoteAgendaDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : RemoteAgendaDataSource {

    // Reminders
    override suspend fun createReminder(
        reminder: AgendaItem.Reminder,
    ): EmptyResult<DataError.Network> {
        return httpClient.post(
            route = AgendaRoutes.REMINDER,
            body = reminder.toUpsertReminderRequest(),
        )
    }

    override suspend fun getReminder(id: String): Result<AgendaItem.Reminder, DataError.Network> {
        return httpClient.get<ReminderDto>(
            route = AgendaRoutes.REMINDER,
            queryParameters = mapOf(
                REMINDER_ID_QUERY_PARAM to id,
            ),
        ).map { it.toReminder() }
    }

    override suspend fun getAllReminders(): Result<List<AgendaItem.Reminder>, DataError.Network> {
        return httpClient.get<AgendaDto>(
            route = AgendaRoutes.FULL_AGENDA,
        )
            .map { agendaDto ->
                agendaDto.reminders.map { it.toReminder() }
            }
    }

    override suspend fun updateReminder(
        reminder: AgendaItem.Reminder,
    ): EmptyResult<DataError.Network> {
        return httpClient.put(
            route = AgendaRoutes.REMINDER,
            body = reminder.toUpsertReminderRequest(),
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

    // Tasks
    override suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError.Network> {
        return httpClient.post(
            route = AgendaRoutes.TASK,
            body = task.toUpsertTaskRequest(),
        )
    }

    override suspend fun getTask(id: String): Result<AgendaItem.Task, DataError.Network> {
        return httpClient.get<TaskDto>(
            route = AgendaRoutes.TASK,
            queryParameters = mapOf(
                TASK_ID_QUERY_PARAM to id,
            ),
        ).map { it.toTask() }
    }

    override suspend fun getAllTasks(): Result<List<AgendaItem.Task>, DataError.Network> {
        return httpClient.get<AgendaDto>(
            route = AgendaRoutes.FULL_AGENDA,
        )
            .map { agendaDto ->
                agendaDto.tasks.map { it.toTask() }
            }
    }

    override suspend fun updateTask(task: AgendaItem.Task): EmptyResult<DataError.Network> {
        return httpClient.put(
            route = AgendaRoutes.TASK,
            body = task.toUpsertTaskRequest(),
        )
    }

    override suspend fun deleteTask(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = AgendaRoutes.TASK,
            queryParameters = mapOf(
                TASK_ID_QUERY_PARAM to id,
            ),
        )
    }

    // All
    override suspend fun getAllAgendaItems(): Result<List<AgendaItem>, DataError.Network> {
        return httpClient.get<AgendaDto>(
            route = AgendaRoutes.FULL_AGENDA,
        ).map { agendaDto ->
            (agendaDto.reminders.map { it.toReminder() } + agendaDto.tasks.map { it.toTask() })
                .sortedBy { it.startDateTime }
        }
    }

    companion object {
        const val REMINDER_ID_QUERY_PARAM = "reminderId"
        const val TASK_ID_QUERY_PARAM = "taskId"
    }
}
