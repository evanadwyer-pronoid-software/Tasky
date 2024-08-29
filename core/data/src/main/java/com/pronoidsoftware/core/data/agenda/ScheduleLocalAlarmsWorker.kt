package com.pronoidsoftware.core.data.agenda

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.agendaitem.AlarmScheduler
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class ScheduleLocalAlarmsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dispatchers: DispatcherProvider,
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {
            alarmScheduler.scheduleAll(localAgendaDataSource.getAllAgendaItemsSnapshot())
            Result.success()
        }
    }
}
