package com.pronoidsoftware.core.data.agenda

import com.pronoidsoftware.core.domain.agendaitem.LocalReminderDataSource
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.ReminderRepository
import com.pronoidsoftware.core.domain.agendaitem.RemoteReminderDataSource
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.asEmptyResult
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstReminderRepository @Inject constructor(
    private val localReminderDataSource: LocalReminderDataSource,
    private val remoteReminderDataSource: RemoteReminderDataSource,
    private val applicationScope: CoroutineScope,
) : ReminderRepository {
    override fun getReminders(): Flow<List<Reminder>> {
        return localReminderDataSource.getAllReminders()
    }

    override suspend fun fetchAllReminders(): EmptyResult<DataError> {
        return when (val result = remoteReminderDataSource.getAllReminders()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    localReminderDataSource.upsertReminders(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun upsertReminder(reminder: Reminder): EmptyResult<DataError> {
        val localResult = localReminderDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }

        val reminderWithId = reminder.copy(id = localResult.data)
        return remoteReminderDataSource.postReminder(
            reminder = reminderWithId,
        )
    }

    override suspend fun deleteReminder(id: ReminderId) {
        localReminderDataSource.deleteReminder(id)
        val remoteResult = applicationScope.async {
            remoteReminderDataSource.deleteReminder(id)
        }.await()
    }
}
