package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias ReminderId = String

interface LocalReminderDataSource {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getRemindersForDate(targetDate: String): Flow<List<Reminder>>
    suspend fun upsertReminder(reminder: Reminder): Result<ReminderId, DataError.Local>
    suspend fun upsertReminders(
        reminders: List<Reminder>,
    ): Result<List<ReminderId>, DataError.Local>

    suspend fun deleteReminder(id: String)
    suspend fun deleteAllReminders()
}
