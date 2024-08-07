package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result

interface RemoteAgendaDataSource {
    // Reminders
    suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError.Network>
    suspend fun getReminder(id: String): Result<AgendaItem.Reminder, DataError.Network>
    suspend fun getAllReminders(): Result<List<AgendaItem.Reminder>, DataError.Network>
    suspend fun updateReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError.Network>
    suspend fun deleteReminder(id: String): EmptyResult<DataError.Network>

    // Tasks
    suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError.Network>
    suspend fun getTask(id: String): Result<AgendaItem.Task, DataError.Network>
    suspend fun getAllTasks(): Result<List<AgendaItem.Task>, DataError.Network>
    suspend fun updateTask(task: AgendaItem.Task): EmptyResult<DataError.Network>
    suspend fun deleteTask(id: String): EmptyResult<DataError.Network>

    // Events
    suspend fun createEvent(event: AgendaItem.Event): EmptyResult<DataError.Network>
    suspend fun getEvent(id: String): Result<AgendaItem.Event, DataError.Network>
    suspend fun getAllEvents(): Result<List<AgendaItem.Event>, DataError.Network>
    suspend fun updateEvent(event: AgendaItem.Event): EmptyResult<DataError.Network>
    suspend fun deleteEvent(id: String): EmptyResult<DataError.Network>

    // All
    suspend fun getAllAgendaItems(): Result<List<AgendaItem>, DataError.Network>
}
