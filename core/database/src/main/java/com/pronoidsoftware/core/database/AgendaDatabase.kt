package com.pronoidsoftware.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.entity.AttendeeEntity
import com.pronoidsoftware.core.database.entity.EventEntity
import com.pronoidsoftware.core.database.entity.PhotoEntity
import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.database.entity.TaskEntity

@Database(
    entities = [
        ReminderEntity::class,
        TaskEntity::class,
        EventEntity::class,
        AttendeeEntity::class,
        PhotoEntity::class,
    ],
    version = 1,
)
abstract class AgendaDatabase : RoomDatabase() {
    abstract val agendaDao: AgendaDao

    companion object {
        const val AGENDA_DATABASE_NAME = "agenda.db"
    }
}
