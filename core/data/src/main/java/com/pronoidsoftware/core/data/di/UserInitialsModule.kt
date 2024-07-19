package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.util.initializeAndCapitalize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(SingletonComponent::class)
object UserInitialsModule {

    @Provides
    @Named("User Initials")
    fun provideUserInitials(sessionStorage: SessionStorage): String? {
        return runBlocking {
            sessionStorage.get()?.fullName?.initializeAndCapitalize()
        }
    }
}
