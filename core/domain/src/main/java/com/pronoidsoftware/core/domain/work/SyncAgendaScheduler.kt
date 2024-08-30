package com.pronoidsoftware.core.domain.work

import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import kotlin.time.Duration

interface SyncAgendaScheduler {

    suspend fun scheduleSync(type: SyncType)
    suspend fun cancelAllSyncs()

    sealed interface SyncType {
        data class CreateReminder(val reminder: AgendaItem.Reminder) : SyncType
        data class FetchReminders(val interval: Duration) : SyncType
        data class UpdateReminder(val reminder: AgendaItem.Reminder) : SyncType
        data class DeleteReminder(val reminderId: ReminderId) : SyncType
    }
}
