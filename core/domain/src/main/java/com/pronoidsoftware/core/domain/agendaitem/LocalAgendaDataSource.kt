package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias ReminderId = String
typealias TaskId = String
typealias EventId = String

interface LocalAgendaDataSource {
    // Reminders
    fun getAllReminders(): Flow<List<AgendaItem.Reminder>>
    fun getRemindersForDate(targetDate: String): Flow<List<AgendaItem.Reminder>>
    suspend fun upsertReminder(reminder: AgendaItem.Reminder): Result<ReminderId, DataError.Local>
    suspend fun upsertReminders(
        reminders: List<AgendaItem.Reminder>,
    ): Result<List<ReminderId>, DataError.Local>

    suspend fun deleteReminder(id: String)
    suspend fun deleteAllReminders()

    // Tasks
    fun getAllTasks(): Flow<List<AgendaItem.Task>>
    fun getTasksForDate(targetDate: String): Flow<List<AgendaItem.Task>>
    suspend fun upsertTask(task: AgendaItem.Task): Result<TaskId, DataError.Local>
    suspend fun upsertTasks(tasks: List<AgendaItem.Task>): Result<List<TaskId>, DataError.Local>

    suspend fun deleteTask(id: String)
    suspend fun deleteAllTasks()

    // Events
    fun getAllEvents(): Flow<List<AgendaItem.Event>>
    fun getEventsForDate(targetDate: String): Flow<List<AgendaItem.Event>>
    suspend fun upsertEvent(event: AgendaItem.Event): Result<EventId, DataError.Local>
    suspend fun upsertEvents(events: List<AgendaItem.Event>): Result<List<EventId>, DataError.Local>

    suspend fun deleteEvent(id: String)
    suspend fun deleteAllEvents()

    // All
    suspend fun upsertAgendaItems(
        reminders: List<AgendaItem.Reminder>,
        tasks: List<AgendaItem.Task>,
        events: List<AgendaItem.Event>,
    ): Result<Map<AgendaItemType, List<String>>, DataError.Local>

    fun getAllAgendaItems(): Flow<List<AgendaItem>>
    fun getAgendaItemsForDate(targetDate: String): Flow<List<AgendaItem>>
    suspend fun deleteAllAgendaItems()
}
