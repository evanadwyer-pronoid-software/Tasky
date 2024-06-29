package com.pronoidsoftware.testutil.androidtest.core.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.pronoidsoftware.core.data.di.CoreDataModule
import com.pronoidsoftware.core.data.networking.HttpClientFactory
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.testutil.jvmtest.core.data.networking.MockHttpClientEngineFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoreDataModule::class],
)
object TestCoreDataModule {

    @Provides
    @Singleton
    fun provideMockEngine(): HttpClientEngine {
        return MockHttpClientEngineFactory().create()
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        sessionStorage: SessionStorage,
        httpClientEngine: HttpClientEngine,
    ): HttpClient {
        return HttpClientFactory(
            sessionStorage = sessionStorage,
            engine = httpClientEngine,
        ).build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return EncryptedSharedPreferences(
            context,
            "com.pronoidsoftware.tasky.test",
            MasterKey(context),
        )
    }
}
