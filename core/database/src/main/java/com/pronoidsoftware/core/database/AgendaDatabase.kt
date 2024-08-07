package com.pronoidsoftware.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.entity.ReminderEntity

@Database(
    entities = [
        ReminderEntity::class,
    ],
    version = 1,
)
abstract class AgendaDatabase : RoomDatabase() {
    abstract val agendaDao: AgendaDao

    companion object {
        const val AGENDA_DATABASE_NAME = "agenda.db"
    }
}
