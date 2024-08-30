package com.pronoidsoftware.core.database.di

import android.app.Application
import androidx.room.Room
import com.pronoidsoftware.core.database.AgendaDatabase
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAgendaDatabase(app: Application): AgendaDatabase {
        return Room.databaseBuilder(
            app,
            AgendaDatabase::class.java,
            AgendaDatabase.AGENDA_DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideAgendaDao(agendaDatabase: AgendaDatabase): AgendaDao {
        return agendaDatabase.agendaDao
    }

    @Provides
    @Singleton
    fun provideAgendaPendingSyncDao(agendaDatabase: AgendaDatabase): AgendaPendingSyncDao {
        return agendaDatabase.agendaPendingSyncDao
    }
}
