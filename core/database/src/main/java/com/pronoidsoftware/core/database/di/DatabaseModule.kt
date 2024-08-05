package com.pronoidsoftware.core.database.di

import android.app.Application
import androidx.room.Room
import com.pronoidsoftware.core.database.ReminderDatabase
import com.pronoidsoftware.core.database.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideReminderDatabase(app: Application): ReminderDatabase {
        return Room.databaseBuilder(
            app,
            ReminderDatabase::class.java,
            ReminderDatabase.REMINDER_DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideReminderDao(reminderDatabase: ReminderDatabase): ReminderDao {
        return reminderDatabase.reminderDao
    }
}
