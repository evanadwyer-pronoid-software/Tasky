package com.pronoidsoftware.core.domain.agendaitem

import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result

interface RemoteReminderDataSource {
    suspend fun getReminder(id: String): Result<Reminder, DataError.Network>
    suspend fun getAllReminders(): Result<List<Reminder>, DataError.Network>
    suspend fun postReminder(reminder: Reminder): EmptyResult<DataError.Network>
    suspend fun deleteReminder(id: String): EmptyResult<DataError.Network>
}
