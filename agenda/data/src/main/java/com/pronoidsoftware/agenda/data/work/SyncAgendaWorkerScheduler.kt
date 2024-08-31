package com.pronoidsoftware.agenda.data.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.workDataOf
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.entity.sync.CreatedEventPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedTaskPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedEventSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedTaskSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedEventPendingSyncAttendeeEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedEventPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedTaskPendingSyncEntity
import com.pronoidsoftware.core.database.mappers.toEventEntity
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.database.mappers.toTaskEntity
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import com.pronoidsoftware.core.domain.work.WorkKeys.EVENT_ID
import com.pronoidsoftware.core.domain.work.WorkKeys.REMINDER_ID
import com.pronoidsoftware.core.domain.work.WorkKeys.TASK_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncAgendaWorkerScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : SyncAgendaScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncAgendaScheduler.SyncType) {
        when (type) {
            is SyncAgendaScheduler.SyncType.CreateReminder -> {
                scheduleCreateReminderWorker(type.reminder)
            }

            is SyncAgendaScheduler.SyncType.FetchReminders -> {
                scheduleFetchRemindersWorker(type.interval)
            }

            is SyncAgendaScheduler.SyncType.UpdateReminder -> {
                scheduleUpdateReminderWorker(type.reminder)
            }

            is SyncAgendaScheduler.SyncType.DeleteReminder -> {
                scheduleDeleteReminderWorker(type.reminderId)
            }

            is SyncAgendaScheduler.SyncType.CreateTask -> {
                scheduleCreateTaskWorker(type.task)
            }

            is SyncAgendaScheduler.SyncType.FetchTasks -> {
                scheduleFetchTasksWorker(type.interval)
            }

            is SyncAgendaScheduler.SyncType.UpdateTask -> {
                scheduleUpdateTaskWorker(type.task)
            }

            is SyncAgendaScheduler.SyncType.DeleteTask -> {
                scheduleDeleteTaskWorker(type.taskId)
            }

            is SyncAgendaScheduler.SyncType.CreateEvent -> {
                scheduleCreateEventWorker(type.event)
            }

            is SyncAgendaScheduler.SyncType.FetchEvents -> {
                scheduleFetchEventsWorker(type.interval)
            }

            is SyncAgendaScheduler.SyncType.UpdateEvent -> {
                scheduleUpdateEventWorker(type.event)
            }

            is SyncAgendaScheduler.SyncType.DeleteEvent -> {
                scheduleDeleteEventWorker(type.eventId)
            }

            is SyncAgendaScheduler.SyncType.FetchAllAgendaItems -> {
                scheduleFetchAllAgendaItemsWorker(type.interval)
            }
        }
    }

    private suspend fun scheduleCreateReminderWorker(reminder: AgendaItem.Reminder) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingCreatedReminder = CreatedReminderPendingSyncEntity(
            reminder = reminder.toReminderEntity(),
            userId = userId,
        )
        agendaPendingSyncDao.upsertCreatedReminderPendingSyncEntity(pendingCreatedReminder)

        val workRequest = OneTimeWorkRequestBuilder<CreateReminderWorker>()
            .addTag(CREATE_REMINDER_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    REMINDER_ID to pendingCreatedReminder.reminderId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchRemindersWorker(interval: Duration) {
        val isSyncScheduled = withContext(dispatchers.io) {
            workManager
                .getWorkInfosByTag(FETCH_REMINDER_WORK)
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchRemindersWorker>(
            repeatInterval = interval.toJavaDuration(),
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInitialDelay(
                duration = 1,
                timeUnit = TimeUnit.MINUTES,
            )
            .addTag(FETCH_REMINDER_WORK)
            .build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleUpdateReminderWorker(reminder: AgendaItem.Reminder) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingUpdatedReminder = UpdatedReminderPendingSyncEntity(
            reminder = reminder.toReminderEntity(),
            userId = userId,
        )
        agendaPendingSyncDao.upsertUpdatedReminderPendingSyncEntity(pendingUpdatedReminder)

        val workRequest = OneTimeWorkRequestBuilder<UpdateReminderWorker>()
            .addTag(UPDATE_REMINDER_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    REMINDER_ID to pendingUpdatedReminder.reminderId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteReminderWorker(reminderId: ReminderId) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = DeletedReminderSyncEntity(
            reminderId = reminderId,
            userId = userId,
        )
        agendaPendingSyncDao.upsertDeletedReminderSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteReminderWorker>()
            .addTag(DELETE_REMINDER_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    REMINDER_ID to entity.reminderId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleCreateTaskWorker(task: AgendaItem.Task) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingCreatedTask = CreatedTaskPendingSyncEntity(
            task = task.toTaskEntity(),
            userId = userId,
        )
        agendaPendingSyncDao.upsertCreatedTaskPendingSyncEntity(pendingCreatedTask)

        val workRequest = OneTimeWorkRequestBuilder<CreateTaskWorker>()
            .addTag(CREATE_TASK_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    TASK_ID to pendingCreatedTask.taskId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchTasksWorker(interval: Duration) {
        val isSyncScheduled = withContext(dispatchers.io) {
            workManager
                .getWorkInfosByTag(FETCH_TASK_WORK)
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchTasksWorker>(
            repeatInterval = interval.toJavaDuration(),
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInitialDelay(
                duration = 1,
                timeUnit = TimeUnit.MINUTES,
            )
            .addTag(FETCH_TASK_WORK)
            .build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleUpdateTaskWorker(task: AgendaItem.Task) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingUpdatedTask = UpdatedTaskPendingSyncEntity(
            task = task.toTaskEntity(),
            userId = userId,
        )
        agendaPendingSyncDao.upsertUpdatedTaskPendingSyncEntity(pendingUpdatedTask)

        val workRequest = OneTimeWorkRequestBuilder<UpdateTaskWorker>()
            .addTag(UPDATE_TASK_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    TASK_ID to pendingUpdatedTask.taskId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteTaskWorker(taskId: TaskId) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = DeletedTaskSyncEntity(
            taskId = taskId,
            userId = userId,
        )
        agendaPendingSyncDao.upsertDeletedTaskSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteTaskWorker>()
            .addTag(DELETE_TASK_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    TASK_ID to entity.taskId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleCreateEventWorker(event: AgendaItem.Event) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingCreatedEvent = CreatedEventPendingSyncEntity(
            event = event.toEventEntity(),
            userId = userId,
        )
        agendaPendingSyncDao.upsertCreatedEventPendingSyncEntity(pendingCreatedEvent)

        val workRequest = OneTimeWorkRequestBuilder<CreatePendingEventWorker>()
            .addTag(CREATE_EVENT_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    EVENT_ID to pendingCreatedEvent.eventId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchEventsWorker(interval: Duration) {
        val isSyncScheduled = withContext(dispatchers.io) {
            workManager
                .getWorkInfosByTag(FETCH_EVENT_WORK)
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchEventsWorker>(
            repeatInterval = interval.toJavaDuration(),
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInitialDelay(
                duration = 1,
                timeUnit = TimeUnit.MINUTES,
            )
            .addTag(FETCH_EVENT_WORK)
            .build()

        workManager.enqueue(workRequest).await()
    }

    private suspend fun scheduleUpdateEventWorker(event: AgendaItem.Event) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingUpdatedEvent = UpdatedEventPendingSyncEntity(
            event = event.toEventEntity(),
            userId = userId,
        )
        val pendingUpdatedEventAttendees = event.attendees.map {
            UpdatedEventPendingSyncAttendeeEntity(
                eventId = event.id,
                attendeeId = it.userId,
            )
        }
        agendaPendingSyncDao.upsertUpdatedEventPendingSyncEntity(pendingUpdatedEvent)
        agendaPendingSyncDao.upsertUpdatedEventPendingSyncAttendeeEntities(
            pendingUpdatedEventAttendees,
        )

        val workRequest = OneTimeWorkRequestBuilder<UpdatePendingEventWorker>()
            .addTag(UPDATE_EVENT_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    EVENT_ID to pendingUpdatedEvent.eventId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteEventWorker(eventId: EventId) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = DeletedEventSyncEntity(
            eventId = eventId,
            userId = userId,
        )
        agendaPendingSyncDao.upsertDeletedEventSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeletePendingEventWorker>()
            .addTag(DELETE_EVENT_WORK)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInputData(
                workDataOf(
                    EVENT_ID to entity.eventId,
                ),
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchAllAgendaItemsWorker(interval: Duration) {
        val isSyncScheduled = withContext(dispatchers.io) {
            workManager
                .getWorkInfosByTag(FETCH_ALL_AGENDA_ITEMS_WORK)
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) {
            return
        }

        val workRequest = PeriodicWorkRequestBuilder<FetchAllAgendaItemsWorker>(
            repeatInterval = interval.toJavaDuration(),
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 10L,
                timeUnit = TimeUnit.SECONDS,
            )
            .setInitialDelay(
                duration = 1,
                timeUnit = TimeUnit.MINUTES,
            )
            .addTag(FETCH_ALL_AGENDA_ITEMS_WORK)
            .build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    companion object {
        const val CREATE_REMINDER_WORK = "CREATE_REMINDER_WORK"
        const val FETCH_REMINDER_WORK = "FETCH_REMINDER_WORK"
        const val UPDATE_REMINDER_WORK = "UPDATE_REMINDER_WORK"
        const val DELETE_REMINDER_WORK = "DELETE_REMINDER_WORK"
        const val CREATE_TASK_WORK = "CREATE_TASK_WORK"
        const val FETCH_TASK_WORK = "FETCH_TASK_WORK"
        const val UPDATE_TASK_WORK = "UPDATE_TASK_WORK"
        const val DELETE_TASK_WORK = "DELETE_TASK_WORK"
        const val CREATE_EVENT_WORK = "CREATE_EVENT_WORK"
        const val FETCH_EVENT_WORK = "FETCH_EVENT_WORK"
        const val UPDATE_EVENT_WORK = "UPDATE_EVENT_WORK"
        const val DELETE_EVENT_WORK = "DELETE_EVENT_WORK"
        const val FETCH_ALL_AGENDA_ITEMS_WORK = "FETCH_ALL_AGENDA_ITEMS_WORK"
    }
}
