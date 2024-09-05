package com.pronoidsoftware.agenda.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.data.work.toWorkerResult
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.mappers.toEvent
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.work.WorkKeys.EVENT_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdatePendingEventWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val remoteAgendaDateSource: RemoteAgendaDataSource,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingUpdateEventId = params.inputData.getString(EVENT_ID)
            ?: return Result.failure()
        val pendingUpdateEventEntity = agendaPendingSyncDao
            .getUpdatedEventPendingSyncEntityWithAttendeeIds(pendingUpdateEventId)
            ?: return Result.failure()
        val event = pendingUpdateEventEntity.toEvent()
        return when (val result = remoteAgendaDateSource.updateEventSync(event)) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                agendaPendingSyncDao.deleteUpdatedEventPendingSyncEntity(pendingUpdateEventId)
                agendaPendingSyncDao.deleteUpdatedEventPendingSyncAttendeeEntities(
                    pendingUpdateEventId,
                )
                Result.success()
            }
        }
    }
}
