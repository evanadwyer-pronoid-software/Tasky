package com.pronoidsoftware.agenda.data.di

import com.pronoidsoftware.agenda.data.work.SyncAgendaWorkerScheduler
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncAgendaSchedulerModule {

    @Binds
    @Singleton
    abstract fun bindSyncAgendaWorkerScheduler(
        syncAgendaWorkerScheduler: SyncAgendaWorkerScheduler,
    ): SyncAgendaScheduler
}
