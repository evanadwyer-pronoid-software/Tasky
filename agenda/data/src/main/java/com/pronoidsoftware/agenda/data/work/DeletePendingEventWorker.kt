package com.pronoidsoftware.agenda.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.data.work.toWorkerResult
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.work.WorkKeys.EVENT_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DeletePendingEventWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val remoteAgendaDataSource: RemoteAgendaDataSource,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val eventId = params.inputData.getString(EVENT_ID) ?: return Result.failure()
        return when (val result = remoteAgendaDataSource.deleteEvent(eventId)) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                agendaPendingSyncDao.deleteDeletedEventSyncEntity(eventId)
                Result.success()
            }
        }
    }
}
