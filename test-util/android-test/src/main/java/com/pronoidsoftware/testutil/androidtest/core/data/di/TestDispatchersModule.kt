package com.pronoidsoftware.testutil.androidtest.core.data.di

import com.pronoidsoftware.core.data.StandardDispatchers
import com.pronoidsoftware.core.data.di.DispatchersModule
import com.pronoidsoftware.core.domain.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
object TestDispatchersModule {

    @Provides
    @Singleton
    fun bindTestDispatcherProvider(): DispatcherProvider {
        return StandardDispatchers
    }
}
