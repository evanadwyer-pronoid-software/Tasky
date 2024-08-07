package com.pronoidsoftware.agenda.network.di

import com.pronoidsoftware.agenda.network.KtorRemoteAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.RemoteAgendaDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteAgendaDataSourceModule {

    @Binds
    @Singleton
    abstract fun provideKtorRemoteAgendaDataSource(
        ktorRemoteAgendaDataSource: KtorRemoteAgendaDataSource,
    ): RemoteAgendaDataSource
}
