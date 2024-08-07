package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    fun getReminders(): Flow<List<AgendaItem.Reminder>>
    suspend fun fetchAllReminders(): EmptyResult<DataError>
    suspend fun updateReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError>
    suspend fun deleteReminder(id: ReminderId)
}
