package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.agenda.alarm.AgendaAlarmScheduler
import com.pronoidsoftware.core.domain.agendaitem.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmSchedulerModule {

    @Binds
    @Singleton
    abstract fun bindAgendaAlarmSchedule(
        agendaAlarmScheduler: AgendaAlarmScheduler,
    ): AlarmScheduler
}
