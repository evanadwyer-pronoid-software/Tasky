package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {

    // Reminders
    suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    fun getReminders(): Flow<List<AgendaItem.Reminder>>
    suspend fun fetchAllReminders(): EmptyResult<DataError>
    suspend fun updateReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    suspend fun deleteReminder(id: ReminderId)

    // Tasks
    suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError>
    fun getTasks(): Flow<List<AgendaItem.Task>>
    suspend fun fetchAllTasks(): EmptyResult<DataError>
    suspend fun updateTask(task: AgendaItem.Task): EmptyResult<DataError>
    suspend fun deleteTask(id: TaskId)

    // Events
    suspend fun createEvent(event: AgendaItem.Event): EmptyResult<DataError>
    fun getEvents(): Flow<List<AgendaItem.Event>>
    suspend fun fetchAllEvents(): EmptyResult<DataError>
    suspend fun updateEvent(event: AgendaItem.Event): EmptyResult<DataError>
    suspend fun deleteEvent(id: EventId)

    // All
    fun getAllAgendaItems(): Flow<List<AgendaItem>>
    suspend fun fetchAllAgendaItems(): EmptyResult<DataError>
}
