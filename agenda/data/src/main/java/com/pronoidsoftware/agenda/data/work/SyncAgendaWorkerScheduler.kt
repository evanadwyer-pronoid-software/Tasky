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
import com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import com.pronoidsoftware.core.domain.work.WorkKeys.REMINDER_ID
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
    }
}
