package com.pronoidsoftware.agenda.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.data.work.toWorkerResult
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.mappers.toTask
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import com.pronoidsoftware.core.domain.work.WorkKeys.TASK_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateTaskWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
    private val remoteAgendaDateSource: RemoteAgendaDataSource,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingUpdateTaskId = params.inputData.getString(TASK_ID)
            ?: return Result.failure()
        val pendingUpdateTaskEntity = agendaPendingSyncDao
            .getUpdatedTaskPendingSyncEntity(pendingUpdateTaskId)
            ?: return Result.failure()
        val task = pendingUpdateTaskEntity.task.toTask()
        return when (val result = remoteAgendaDateSource.updateTask(task)) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                agendaPendingSyncDao.deleteUpdatedTaskPendingSyncEntity(pendingUpdateTaskId)
                Result.success()
            }
        }
    }
}
