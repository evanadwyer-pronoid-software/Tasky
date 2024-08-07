package com.pronoidsoftware.core.database

import android.database.sqlite.SQLiteFullException
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalAgendaDataSource @Inject constructor(
    private val agendaDao: AgendaDao,
) : LocalAgendaDataSource {
    override fun getAllReminders(): Flow<List<Reminder>> {
        return agendaDao.getAllReminders()
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override fun getRemindersForDate(targetDate: String): Flow<List<Reminder>> {
        return agendaDao.getRemindersForDate(targetDate)
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override suspend fun upsertReminder(reminder: Reminder): Result<ReminderId, DataError.Local> {
        return try {
            val entity = reminder.toReminderEntity()
            agendaDao.upsertReminder(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertReminders(
        reminders: List<Reminder>,
    ): Result<List<ReminderId>, DataError.Local> {
        return try {
            val entities = reminders.map { it.toReminderEntity() }
            agendaDao.upsertReminders(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteReminder(id: String) {
        agendaDao.deleteReminder(id)
    }

    override suspend fun deleteAllReminders() {
        agendaDao.deleteAllReminders()
    }
}
