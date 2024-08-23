package com.pronoidsoftware.agenda.network.di

import com.pronoidsoftware.agenda.domain.AttendeeRepository
import com.pronoidsoftware.agenda.network.AttendeeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendeeRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAttendeeRepository(
        attendeeRepository: AttendeeRepositoryImpl,
    ): AttendeeRepository
}
