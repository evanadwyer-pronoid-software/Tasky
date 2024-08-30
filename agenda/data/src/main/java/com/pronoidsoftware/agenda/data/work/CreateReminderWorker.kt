package com.pronoidsoftware.agenda.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.data.work.DataErrorWorkerResult
import com.pronoidsoftware.core.data.work.toWorkerResult
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.work.WorkKeys.REMINDER_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CreateReminderWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val remoteAgendaDateSource: RemoteAgendaDataSource,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingCreateReminderId = params.inputData.getString(REMINDER_ID)
            ?: return Result.failure()
        val pendingCreatedRunEntity = agendaPendingSyncDao
            .getCreatedReminderPendingSyncEntity(pendingCreateReminderId)
            ?: return Result.failure()
        val reminder = pendingCreatedRunEntity.reminder.toReminder()
        return when (val result = remoteAgendaDateSource.createReminder(reminder)) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                when (result.error.toWorkerResult()) {
                    DataErrorWorkerResult.FAILURE -> Result.failure()
                    DataErrorWorkerResult.RETRY -> Result.retry()
                }
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                agendaPendingSyncDao.deleteCreatedReminderPendingSyncEntity(pendingCreateReminderId)
                Result.success()
            }
        }
    }
}
