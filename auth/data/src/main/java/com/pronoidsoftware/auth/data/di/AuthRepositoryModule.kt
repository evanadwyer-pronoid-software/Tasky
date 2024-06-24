package com.pronoidsoftware.auth.data.di

import com.pronoidsoftware.auth.data.AuthRepositoryImplementation
import com.pronoidsoftware.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImplementation: AuthRepositoryImplementation,
    ): AuthRepository
}
