package com.pronoidsoftware.core.data.agenda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sessionStorage: SessionStorage

    @Inject
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var agendaRepository: AgendaRepository

    @Inject
    lateinit var syncAgendaScheduler: SyncAgendaScheduler

    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            applicationScope.launch {
                sessionStorage.get()?.userId?.let {
                    context?.let {
                        val scheduleLocalAlarmsWorkRequest =
                            OneTimeWorkRequestBuilder<ScheduleLocalAlarmsWorker>()
                                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                                .build()
                        WorkManager.getInstance(context).enqueue(scheduleLocalAlarmsWorkRequest)

                        agendaRepository.syncPendingReminders()
                        agendaRepository.syncPendingTasks()
                        agendaRepository.syncPendingEvents()
                        syncAgendaScheduler.scheduleSync(
                            type = SyncAgendaScheduler.SyncType.FetchReminders(30.minutes),
                        )
                        syncAgendaScheduler.scheduleSync(
                            type = SyncAgendaScheduler.SyncType.FetchTasks(30.minutes),
                        )
                    }
                }
            }
        }
    }
}
