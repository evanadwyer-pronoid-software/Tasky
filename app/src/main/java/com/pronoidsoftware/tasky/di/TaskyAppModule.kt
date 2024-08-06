package com.pronoidsoftware.tasky.di

import android.content.Context
import com.pronoidsoftware.tasky.TaskyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object TaskyAppModule {

    @Provides
    @Singleton
    fun provideApplicationScope(@ApplicationContext context: Context): CoroutineScope {
        return (context.applicationContext as TaskyApp).applicationScope
    }
}
