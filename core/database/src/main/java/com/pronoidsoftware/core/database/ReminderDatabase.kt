package com.pronoidsoftware.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pronoidsoftware.core.database.dao.ReminderDao
import com.pronoidsoftware.core.database.entity.ReminderEntity

@Database(
    entities = [
        ReminderEntity::class,
    ],
    version = 1,
)
abstract class ReminderDatabase : RoomDatabase() {
    abstract val reminderDao: ReminderDao

    companion object {
        const val REMINDER_DATABASE_NAME = "reminder.db"
    }
}
