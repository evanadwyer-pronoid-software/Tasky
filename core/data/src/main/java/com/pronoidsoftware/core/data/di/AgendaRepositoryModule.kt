package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.agenda.OfflineFirstAgendaRepository
import com.pronoidsoftware.core.domain.agendaitem.AgendaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AgendaRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOfflineFirstAgendaRepository(
        offlineFirstAgendaRepository: OfflineFirstAgendaRepository,
    ): AgendaRepository
}
