package com.pronoidsoftware.agenda.network.di

import com.pronoidsoftware.agenda.network.NetworkConnectivityObserver
import com.pronoidsoftware.core.domain.ConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkConnectivityObserverModule {

    @Binds
    @Singleton
    abstract fun provideNetworkConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver,
    ): ConnectivityObserver
}
