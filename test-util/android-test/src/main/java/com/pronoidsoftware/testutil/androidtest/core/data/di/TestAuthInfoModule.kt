package com.pronoidsoftware.testutil.androidtest.core.data.di

import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.testutil.jvmtest.core.data.auth.authInfoStub
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAuthInfoModule {

    @Provides
    @Singleton
    fun provideTestAuthInfo(): AuthInfo {
        return authInfoStub()
    }
}
