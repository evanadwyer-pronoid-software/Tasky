package com.pronoidsoftware.core.data.agenda

import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.ReminderRepository
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.asEmptyResult
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstReminderRepository @Inject constructor(
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val remoteAgendaDataSource: RemoteAgendaDataSource,
    private val applicationScope: CoroutineScope,
) : ReminderRepository {
    override suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }

        val reminderWithId = reminder.copy(id = localResult.data)
        return remoteAgendaDataSource.createReminder(
            reminder = reminderWithId,
        )
    }

    override fun getReminders(): Flow<List<AgendaItem.Reminder>> {
        return localAgendaDataSource.getAllReminders()
    }

    override suspend fun fetchAllReminders(): EmptyResult<DataError> {
        return when (val result = remoteAgendaDataSource.getAllReminders()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    localAgendaDataSource.upsertReminders(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun updateReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }

        val reminderWithId = reminder.copy(id = localResult.data)
        return remoteAgendaDataSource.updateReminder(
            reminder = reminderWithId,
        )
    }

    override suspend fun deleteReminder(id: ReminderId) {
        localAgendaDataSource.deleteReminder(id)
        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteReminder(id)
        }.await()
    }
}
