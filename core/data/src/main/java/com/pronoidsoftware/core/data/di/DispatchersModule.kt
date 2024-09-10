package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.StandardDispatchers
import com.pronoidsoftware.core.domain.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Singleton
    fun bindDispatcherProvider(): DispatcherProvider {
        return StandardDispatchers
    }
}
