package com.pronoidsoftware.testutil.androidtest.core.data.di

import android.content.SharedPreferences
import com.pronoidsoftware.core.data.auth.EncryptedSessionStorage
import com.pronoidsoftware.core.data.di.SessionStorageModule
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionStorageModule::class],
)
object TestSessionStorageModule {

    @Provides
    @Singleton
    fun provideTestSessionStorage(
        sharedPreferences: SharedPreferences,
        authInfo: AuthInfo?,
    ): SessionStorage {
        val sessionStorage = EncryptedSessionStorage(sharedPreferences)
        runBlocking {
            sessionStorage.set(authInfo)
        }
        return sessionStorage
    }
}
