package com.pronoidsoftware.core.data.agenda

import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.asEmptyResult
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstAgendaRepository @Inject constructor(
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val remoteAgendaDataSource: RemoteAgendaDataSource,
    private val applicationScope: CoroutineScope,
) : AgendaRepository {

    // Reminders
    override suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        return remoteAgendaDataSource.createReminder(reminder)
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
        return remoteAgendaDataSource.updateReminder(reminder)
    }

    override suspend fun deleteReminder(id: ReminderId) {
        localAgendaDataSource.deleteReminder(id)
        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteReminder(id)
        }.await()
    }

    // Tasks
    override suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertTask(task)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        return remoteAgendaDataSource.createTask(task)
    }

    override fun getTasks(): Flow<List<AgendaItem.Task>> {
        return localAgendaDataSource.getAllTasks()
    }

    override suspend fun fetchAllTasks(): EmptyResult<DataError> {
        return when (val result = remoteAgendaDataSource.getAllTasks()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    localAgendaDataSource.upsertTasks(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun updateTask(task: AgendaItem.Task): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertTask(task)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        return remoteAgendaDataSource.updateTask(task)
    }

    override suspend fun deleteTask(id: TaskId) {
        localAgendaDataSource.deleteTask(id)
        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteTask(id)
        }.await()
    }

    // All
    override fun getAllAgendaItems(): Flow<List<AgendaItem>> {
        return localAgendaDataSource.getAllAgendaItems()
    }

    override suspend fun fetchAllAgendaItems(): EmptyResult<DataError> {
        return when (val result = remoteAgendaDataSource.getAllAgendaItems()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    val reminders = result.data.filterIsInstance<AgendaItem.Reminder>()
                    val tasks = result.data.filterIsInstance<AgendaItem.Task>()
                    val events = result.data.filterIsInstance<AgendaItem.Event>()
                    localAgendaDataSource.upsertAgendaItems(
                        reminders = reminders,
                        tasks = tasks,
                        events = events,
                    ).asEmptyResult()
                }.await()
            }
        }
    }
}
