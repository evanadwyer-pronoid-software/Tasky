package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.agenda.OfflineFirstReminderRepository
import com.pronoidsoftware.core.domain.agendaitem.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOfflineFirstReminderRepository(
        offlineFirstReminderRepository: OfflineFirstReminderRepository,
    ): ReminderRepository
}
