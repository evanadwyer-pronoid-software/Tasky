package com.pronoidsoftware.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.entity.AttendeeEntity
import com.pronoidsoftware.core.database.entity.EventEntity
import com.pronoidsoftware.core.database.entity.PhotoEntity
import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.database.entity.TaskEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedTaskPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedEventSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedTaskSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedTaskPendingSyncEntity

@Database(
    entities = [
        ReminderEntity::class,
        CreatedReminderPendingSyncEntity::class,
        UpdatedReminderPendingSyncEntity::class,
        DeletedReminderSyncEntity::class,
        TaskEntity::class,
        CreatedTaskPendingSyncEntity::class,
        UpdatedTaskPendingSyncEntity::class,
        DeletedTaskSyncEntity::class,
        EventEntity::class,
        DeletedEventSyncEntity::class,
        AttendeeEntity::class,
        PhotoEntity::class,
    ],
    version = 1,
)
abstract class AgendaDatabase : RoomDatabase() {
    abstract val agendaDao: AgendaDao
    abstract val agendaPendingSyncDao: AgendaPendingSyncDao

    companion object {
        const val AGENDA_DATABASE_NAME = "agenda.db"
    }
}
