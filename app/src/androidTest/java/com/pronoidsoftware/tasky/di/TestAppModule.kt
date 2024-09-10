package com.pronoidsoftware.tasky.di

import com.pronoidsoftware.core.domain.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TaskyAppModule::class],
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideTestApplicationScope(dispatchers: DispatcherProvider): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatchers.default)
    }
}
