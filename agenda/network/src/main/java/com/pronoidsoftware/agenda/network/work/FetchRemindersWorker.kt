package com.pronoidsoftware.agenda.network.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchRemindersWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val agendaRepository: AgendaRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = agendaRepository.fetchAllReminders()) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                Result.success()
            }
        }
    }
}
