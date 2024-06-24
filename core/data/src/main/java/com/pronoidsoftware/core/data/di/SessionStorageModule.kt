package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.auth.EncryptedSessionStorage
import com.pronoidsoftware.core.domain.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionStorageModule {

    @Binds
    @Singleton
    abstract fun bindEncryptedSessionStorage(
        encryptedSessionStorage: EncryptedSessionStorage,
    ): SessionStorage
}
