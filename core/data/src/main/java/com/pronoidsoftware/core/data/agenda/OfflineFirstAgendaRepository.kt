package com.pronoidsoftware.core.data.agenda

import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedTaskPendingSyncEntity
import com.pronoidsoftware.core.database.mappers.toEvent
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.database.mappers.toTask
import com.pronoidsoftware.core.database.mappers.toTaskEntity
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import com.pronoidsoftware.core.domain.agendaitem.AlarmScheduler
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.EmptyResult
import com.pronoidsoftware.core.domain.util.Result
import com.pronoidsoftware.core.domain.util.asEmptyResult
import com.pronoidsoftware.core.domain.util.onSuccess
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class OfflineFirstAgendaRepository @Inject constructor(
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val remoteAgendaDataSource: RemoteAgendaDataSource,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val dispatchers: DispatcherProvider,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
    private val alarmScheduler: AlarmScheduler,
    private val syncAgendaScheduler: SyncAgendaScheduler,
) : AgendaRepository {

    // Reminders
    override suspend fun createReminder(reminder: AgendaItem.Reminder): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        alarmScheduler.schedule(reminder)
        return when (remoteAgendaDataSource.createReminder(reminder)) {
            is Result.Error -> {
                applicationScope.launch {
                    syncAgendaScheduler.scheduleSync(
                        type = SyncAgendaScheduler.SyncType.CreateReminder(
                            reminder = reminder,
                        ),
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override suspend fun getReminder(id: ReminderId): AgendaItem.Reminder? {
        return localAgendaDataSource.getReminder(id)
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
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.LOGGED_OUT)
        val localResult = localAgendaDataSource.upsertReminder(reminder)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        alarmScheduler.schedule(reminder)
        return when (remoteAgendaDataSource.updateReminder(reminder)) {
            is Result.Error -> {
                val isCreatePendingSync =
                    agendaPendingSyncDao.getCreatedReminderPendingSyncEntity(reminder.id) != null
                if (isCreatePendingSync) {
                    agendaPendingSyncDao.upsertCreatedReminderPendingSyncEntity(
                        CreatedReminderPendingSyncEntity(
                            reminder = reminder.toReminderEntity(),
                            userId = userId,
                        ),
                    )
                } else {
                    applicationScope.launch {
                        syncAgendaScheduler.scheduleSync(
                            type = SyncAgendaScheduler.SyncType.UpdateReminder(
                                reminder = reminder,
                            ),
                        )
                    }.join()
                }
                Result.Success(Unit)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override suspend fun deleteReminder(id: ReminderId) {
        localAgendaDataSource.deleteReminder(id)
        alarmScheduler.cancel(id)

        agendaPendingSyncDao.deleteUpdatedReminderPendingSyncEntity(id)
        val isCreatePendingSync =
            agendaPendingSyncDao.getCreatedReminderPendingSyncEntity(id) != null
        if (isCreatePendingSync) {
            agendaPendingSyncDao.deleteCreatedReminderPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteReminder(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncAgendaScheduler.scheduleSync(
                    type = SyncAgendaScheduler.SyncType.DeleteReminder(id),
                )
            }.join()
        }
    }

    override suspend fun syncPendingReminders() {
        withContext(dispatchers.io) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdReminders = async {
                agendaPendingSyncDao.getAllCreatedReminderPendingSyncEntities(userId)
            }

            val updatedReminders = async {
                agendaPendingSyncDao.getAllUpdatedReminderPendingSyncEntities(userId)
            }

            val deletedReminders = async {
                agendaPendingSyncDao.getAllDeletedReminderSyncEntities(userId)
            }

            val createJobs = createdReminders
                .await()
                .map {
                    launch {
                        val reminder = it.reminder.toReminder()
                        when (remoteAgendaDataSource.createReminder(reminder)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteCreatedReminderPendingSyncEntity(
                                        it.reminderId,
                                    )
                                }.join()
                            }
                        }
                    }
                }
            val updateJobs = updatedReminders
                .await()
                .map {
                    launch {
                        val reminder = it.reminder.toReminder()
                        when (remoteAgendaDataSource.updateReminder(reminder)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteUpdatedReminderPendingSyncEntity(
                                        it.reminderId,
                                    )
                                }.join()
                            }
                        }
                    }
                }
            val deleteJobs = deletedReminders
                .await()
                .map {
                    launch {
                        when (remoteAgendaDataSource.deleteReminder(it.reminderId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteDeletedReminderSyncEntity(
                                        it.reminderId,
                                    )
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            updateJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    // Tasks
    override suspend fun createTask(task: AgendaItem.Task): EmptyResult<DataError> {
        val localResult = localAgendaDataSource.upsertTask(task)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        alarmScheduler.schedule(task)
        return when (remoteAgendaDataSource.createTask(task)) {
            is Result.Error -> {
                applicationScope.launch {
                    syncAgendaScheduler.scheduleSync(
                        type = SyncAgendaScheduler.SyncType.CreateTask(
                            task = task,
                        ),
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override suspend fun getTask(id: TaskId): AgendaItem.Task? {
        return localAgendaDataSource.getTask(id)
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
        val userId = sessionStorage.get()?.userId ?: return Result.Error(DataError.Local.LOGGED_OUT)
        val localResult = localAgendaDataSource.upsertTask(task)
        if (localResult !is Result.Success) {
            return localResult.asEmptyResult()
        }
        alarmScheduler.schedule(task)
        return when (remoteAgendaDataSource.updateTask(task)) {
            is Result.Error -> {
                val isCreatePendingSync =
                    agendaPendingSyncDao.getCreatedTaskPendingSyncEntity(task.id) != null
                if (isCreatePendingSync) {
                    agendaPendingSyncDao.upsertCreatedTaskPendingSyncEntity(
                        CreatedTaskPendingSyncEntity(
                            task = task.toTaskEntity(),
                            userId = userId,
                        ),
                    )
                } else {
                    applicationScope.launch {
                        syncAgendaScheduler.scheduleSync(
                            type = SyncAgendaScheduler.SyncType.UpdateTask(
                                task = task,
                            ),
                        )
                    }.join()
                }
                Result.Success(Unit)
            }

            is Result.Success -> {
                Result.Success(Unit)
            }
        }
    }

    override suspend fun deleteTask(id: TaskId) {
        localAgendaDataSource.deleteTask(id)
        alarmScheduler.cancel(id)

        agendaPendingSyncDao.deleteUpdatedTaskPendingSyncEntity(id)
        val isCreatePendingSync = agendaPendingSyncDao.getCreatedTaskPendingSyncEntity(id) != null
        if (isCreatePendingSync) {
            agendaPendingSyncDao.deleteCreatedTaskPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteTask(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncAgendaScheduler.scheduleSync(
                    type = SyncAgendaScheduler.SyncType.DeleteTask(id),
                )
            }.join()
        }
    }

    override suspend fun syncPendingTasks() {
        withContext(dispatchers.io) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdTasks = async {
                agendaPendingSyncDao.getAllCreatedTaskPendingSyncEntities(userId)
            }

            val updatedTasks = async {
                agendaPendingSyncDao.getAllUpdatedTaskPendingSyncEntities(userId)
            }

            val deletedTasks = async {
                agendaPendingSyncDao.getAllDeletedTaskSyncEntities(userId)
            }

            val createJobs = createdTasks
                .await()
                .map {
                    launch {
                        val task = it.task.toTask()
                        when (remoteAgendaDataSource.createTask(task)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteCreatedTaskPendingSyncEntity(
                                        it.taskId,
                                    )
                                }.join()
                            }
                        }
                    }
                }
            val updateJobs = updatedTasks
                .await()
                .map {
                    launch {
                        val task = it.task.toTask()
                        when (remoteAgendaDataSource.updateTask(task)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteUpdatedTaskPendingSyncEntity(
                                        it.taskId,
                                    )
                                }.join()
                            }
                        }
                    }
                }
            val deleteJobs = deletedTasks
                .await()
                .map {
                    launch {
                        when (remoteAgendaDataSource.deleteTask(it.taskId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteDeletedTaskSyncEntity(
                                        it.taskId,
                                    )
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            updateJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    // Events
    override suspend fun createEventLocallyEnqueueRemote(
        event: AgendaItem.Event,
    ): Result<String, DataError> {
        val localResult = localAgendaDataSource.upsertEvent(event)
        if (localResult !is Result.Success) {
            return localResult
        }
        alarmScheduler.schedule(event)
        val eventWorkId = remoteAgendaDataSource.createEventAsync(event)
        return Result.Success(eventWorkId.toString())
    }

    override suspend fun getEvent(id: EventId): AgendaItem.Event? {
        val localEvent = localAgendaDataSource.getEvent(id)
        val sortedAttendees = localEvent?.let { event ->
            val attendees = event.attendees
                .sortedBy { it.fullName }
            attendees.find { it.userId == event.host }?.let { creator ->
                listOf(creator) + attendees.filterNot { it == creator }
            } ?: attendees
        }
        return localEvent?.copy(
            attendees = sortedAttendees ?: emptyList(),
        )
    }

    override fun getEvents(): Flow<List<AgendaItem.Event>> {
        return localAgendaDataSource.getAllEvents()
    }

    override suspend fun fetchAllEvents(): EmptyResult<DataError> {
        return when (val result = remoteAgendaDataSource.getAllEvents()) {
            is Result.Error -> result.asEmptyResult()
            is Result.Success -> {
                applicationScope.async {
                    localAgendaDataSource.upsertEvents(result.data).asEmptyResult()
                }.await()
            }
        }
    }

    override suspend fun updateEventLocallyEnqueueRemote(
        event: AgendaItem.Event,
    ): Result<String, DataError> {
        val localResult = localAgendaDataSource.upsertEvent(event)
        if (localResult !is Result.Success) {
            return localResult
        }
        if (event.isLocalUserGoing) {
            alarmScheduler.schedule(event)
        } else {
            alarmScheduler.cancel(event.id)
        }
        val eventWorkId = remoteAgendaDataSource.updateEventAsync(event)
        return Result.Success(eventWorkId.toString())
    }

    override suspend fun deleteEvent(id: EventId) {
        localAgendaDataSource.deleteEvent(id)
        alarmScheduler.cancel(id)

        val isCreatePendingSync = agendaPendingSyncDao.getCreatedEventPendingSyncEntity(id) != null
        if (isCreatePendingSync) {
            agendaPendingSyncDao.deleteCreatedEventPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteAgendaDataSource.deleteEvent(id)
        }.await()
    }

    override suspend fun removeAttendee(id: EventId) {
        localAgendaDataSource.deleteEvent(id)
        alarmScheduler.cancel(id)
    }

    override suspend fun syncPendingEvents() {
        withContext(dispatchers.io) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdEvents = async {
                agendaPendingSyncDao.getAllCreatedEventPendingSyncEntities(userId)
            }

            val updatedEvents = async {
                agendaPendingSyncDao.getAllUpdatedEventPendingSyncEntitiesWithAttendeeIds(userId)
            }

            val deletedEvents = async {
                agendaPendingSyncDao.getAllDeletedEventSyncEntities(userId)
            }

            val createJobs = createdEvents
                .await()
                .map {
                    launch {
                        val event = it.event.toEvent()
                        when (remoteAgendaDataSource.createEventSync(event)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteCreatedEventPendingSyncEntity(
                                        it.eventId,
                                    )
                                }.join()
                            }
                        }
                    }
                }
            val updateJobs = updatedEvents
                .await()
                .map {
                    launch {
                        val event = it.toEvent()
                        when (remoteAgendaDataSource.updateEventSync(event)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteUpdatedEventPendingSyncEntity(
                                        it.updatedEventPendingSyncEntity.eventId,
                                    )
                                    agendaPendingSyncDao
                                        .deleteUpdatedEventPendingSyncAttendeeEntity(
                                            it.updatedEventPendingSyncEntity.eventId,
                                        )
                                }.join()
                            }
                        }
                    }
                }
            val deleteJobs = deletedEvents
                .await()
                .map {
                    launch {
                        when (remoteAgendaDataSource.deleteEvent(it.eventId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    agendaPendingSyncDao.deleteDeletedEventSyncEntity(
                                        it.eventId,
                                    )
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            updateJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    // All
    override fun getAllAgendaItems(): Flow<List<AgendaItem>> {
        return localAgendaDataSource.getAllAgendaItems()
    }

    override fun getAgendaItemsForDate(targetDate: LocalDate): Flow<List<AgendaItem>> {
        return localAgendaDataSource.getAgendaItemsForDate(targetDate)
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
                    .onSuccess {
                        alarmScheduler.scheduleAll(result.data)
                    }
            }
        }
    }

    override suspend fun deleteAllAgendaItems() {
        val agendaItemIds = localAgendaDataSource.getAllAgendaItemsSnapshot().map { it.id }
        alarmScheduler.cancelAll(agendaItemIds)
        localAgendaDataSource.deleteAllAgendaItems()
    }
}
