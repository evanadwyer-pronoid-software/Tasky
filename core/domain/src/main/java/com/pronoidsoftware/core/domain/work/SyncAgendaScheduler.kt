package com.pronoidsoftware.core.domain.work

import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import kotlin.time.Duration

interface SyncAgendaScheduler {

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class CreateReminder(val reminder: AgendaItem.Reminder) : SyncType
        data class FetchReminders(val interval: Duration) : SyncType
        data class UpdateReminder(val reminder: AgendaItem.Reminder) : SyncType
        data class DeleteReminder(val reminderId: ReminderId) : SyncType
        data class CreateTask(val task: AgendaItem.Task) : SyncType
        data class FetchTasks(val interval: Duration) : SyncType
        data class UpdateTask(val task: AgendaItem.Task) : SyncType
        data class DeleteTask(val taskId: TaskId) : SyncType
        data class CreateEvent(val event: AgendaItem.Event) : SyncType
        data class FetchEvents(val interval: Duration) : SyncType
        data class UpdateEvent(val event: AgendaItem.Event) : SyncType
        data class DeleteEvent(val eventId: EventId) : SyncType
        data class FetchAllAgendaItems(val interval: Duration) : SyncType
    }
}
