package com.pronoidsoftware.core.presentation.ui.di

import com.pronoidsoftware.core.domain.agendaitem.AlarmScheduler
import com.pronoidsoftware.core.presentation.ui.alarm.AgendaAlarmScheduler
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
