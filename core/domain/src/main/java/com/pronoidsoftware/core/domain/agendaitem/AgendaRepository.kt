package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

typealias ReminderId = String
typealias TaskId = String
typealias EventId = String

interface AgendaRepository {

    // Reminders
    suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    suspend fun getReminder(id: ReminderId): AgendaItem.Reminder?
    fun getReminders(): Flow<List<AgendaItem.Reminder>>
    suspend fun fetchAllReminders(): EmptyResult<DataError>
    suspend fun updateReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    suspend fun deleteReminder(id: ReminderId)
    suspend fun syncPendingReminders()

    // Tasks
    suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError>
    suspend fun getTask(id: TaskId): AgendaItem.Task?
    fun getTasks(): Flow<List<AgendaItem.Task>>
    suspend fun fetchAllTasks(): EmptyResult<DataError>
    suspend fun updateTask(task: AgendaItem.Task): EmptyResult<DataError>
    suspend fun deleteTask(id: TaskId)
    suspend fun syncPendingTasks()

    // Events
    suspend fun createEventLocallyEnqueueRemote(event: AgendaItem.Event): Result<String, DataError>
    suspend fun getEvent(id: EventId): AgendaItem.Event?
    fun getEvents(): Flow<List<AgendaItem.Event>>
    suspend fun fetchAllEvents(): EmptyResult<DataError>
    suspend fun updateEventLocallyEnqueueRemote(event: AgendaItem.Event): Result<String, DataError>
    suspend fun deleteEvent(id: EventId)
    suspend fun removeAttendee(id: EventId)

    // All
    fun getAllAgendaItems(): Flow<List<AgendaItem>>
    fun getAgendaItemsForDate(targetDate: LocalDate): Flow<List<AgendaItem>>
    suspend fun fetchAllAgendaItems(): EmptyResult<DataError>
    suspend fun deleteAllAgendaItems()
}
