package com.pronoidsoftware.core.database.di

import com.pronoidsoftware.core.database.RoomLocalReminderDataSource
import com.pronoidsoftware.core.domain.agendaitem.LocalReminderDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalReminderDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRoomLocalReminderDataSource(
        roomLocalReminderDataSource: RoomLocalReminderDataSource,
    ): LocalReminderDataSource
}
