package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getReminders(): Flow<List<Reminder>>
    suspend fun fetchAllReminders(): EmptyResult<DataError>
    suspend fun upsertReminder(reminder: Reminder): EmptyResult<DataError>
    suspend fun deleteReminder(id: ReminderId)
}
