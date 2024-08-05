package com.pronoidsoftware.agenda.network.di

import com.pronoidsoftware.agenda.network.KtorRemoteReminderDataSource
import com.pronoidsoftware.core.domain.agendaitem.RemoteReminderDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteReminderDataSourceModule {

    @Binds
    @Singleton
    abstract fun provideKtorRemoteReminderDataSource(
        ktorRemoteReminderDataSource: KtorRemoteReminderDataSource,
    ): RemoteReminderDataSource
}
