package com.pronoidsoftware.core.database.di

import com.pronoidsoftware.core.database.RoomLocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalAgendaDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRoomLocalAgendaDataSource(
        roomLocalAgendaDataSource: RoomLocalAgendaDataSource,
    ): LocalAgendaDataSource
}
