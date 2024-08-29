package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface LocalAgendaDataSource {
    // Reminders
    suspend fun getReminder(id: ReminderId): AgendaItem.Reminder?
    fun getAllReminders(): Flow<List<AgendaItem.Reminder>>
    suspend fun getAllRemindersSnapshot(): List<AgendaItem.Reminder>
    fun getRemindersForDate(targetDate: LocalDate): Flow<List<AgendaItem.Reminder>>
    suspend fun upsertReminder(reminder: AgendaItem.Reminder): Result<ReminderId, DataError.Local>
    suspend fun upsertReminders(
        reminders: List<AgendaItem.Reminder>,
    ): Result<List<ReminderId>, DataError.Local>

    suspend fun deleteReminder(id: ReminderId)
    suspend fun deleteAllReminders()

    // Tasks
    suspend fun getTask(id: TaskId): AgendaItem.Task?
    fun getAllTasks(): Flow<List<AgendaItem.Task>>
    suspend fun getAllTasksSnapshot(): List<AgendaItem.Task>
    fun getTasksForDate(targetDate: LocalDate): Flow<List<AgendaItem.Task>>
    suspend fun upsertTask(task: AgendaItem.Task): Result<TaskId, DataError.Local>
    suspend fun upsertTasks(tasks: List<AgendaItem.Task>): Result<List<TaskId>, DataError.Local>

    suspend fun deleteTask(id: TaskId)
    suspend fun deleteAllTasks()

    // Events
    suspend fun getEvent(id: EventId): AgendaItem.Event?
    fun getAllEvents(): Flow<List<AgendaItem.Event>>
    suspend fun getAllEventsSnapshot(): List<AgendaItem.Event>
    fun getEventsForDate(targetDate: LocalDate): Flow<List<AgendaItem.Event>>
    suspend fun upsertEvent(event: AgendaItem.Event): Result<EventId, DataError.Local>
    suspend fun upsertEvents(events: List<AgendaItem.Event>): Result<List<EventId>, DataError.Local>

    suspend fun deleteEvent(id: EventId)
    suspend fun deleteAllEvents()

    // All
    suspend fun upsertAgendaItems(
        reminders: List<AgendaItem.Reminder>,
        tasks: List<AgendaItem.Task>,
        events: List<AgendaItem.Event>,
    ): Result<Map<AgendaItemType, List<String>>, DataError.Local>

    fun getAllAgendaItems(): Flow<List<AgendaItem>>
    suspend fun getAllAgendaItemsSnapshot(): List<AgendaItem>
    fun getAgendaItemsForDate(targetDate: LocalDate): Flow<List<AgendaItem>>
    suspend fun deleteAllAgendaItems()
}
