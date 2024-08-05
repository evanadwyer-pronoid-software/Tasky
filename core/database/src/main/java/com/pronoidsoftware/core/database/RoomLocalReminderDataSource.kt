package com.pronoidsoftware.core.database

import android.database.sqlite.SQLiteFullException
import com.pronoidsoftware.core.database.dao.ReminderDao
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.domain.agendaitem.LocalReminderDataSource
import com.pronoidsoftware.core.domain.agendaitem.Reminder
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalReminderDataSource(
    private val reminderDao: ReminderDao,
) : LocalReminderDataSource {
    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override fun getRemindersForDate(targetDate: String): Flow<List<Reminder>> {
        return reminderDao.getRemindersForDate(targetDate)
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override suspend fun upsertReminder(reminder: Reminder): Result<ReminderId, DataError.Local> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)
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
            reminderDao.upsertReminders(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminder(id)
    }

    override suspend fun deleteAllReminders() {
        reminderDao.deleteAllReminders()
    }
}
