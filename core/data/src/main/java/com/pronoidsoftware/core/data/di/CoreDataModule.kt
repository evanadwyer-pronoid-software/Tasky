package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.networking.HttpClientFactory
import com.pronoidsoftware.core.domain.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    @Provides
    @Singleton
    fun provideHttpClient(sessionStorage: SessionStorage): HttpClient {
        return HttpClientFactory(
            sessionStorage = sessionStorage,
        ).build()
    }
}
