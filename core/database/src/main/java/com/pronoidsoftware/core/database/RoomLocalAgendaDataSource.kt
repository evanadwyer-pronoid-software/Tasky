package com.pronoidsoftware.core.database

import android.database.sqlite.SQLiteFullException
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.database.mappers.toTask
import com.pronoidsoftware.core.database.mappers.toTaskEntity
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RoomLocalAgendaDataSource @Inject constructor(
    private val agendaDao: AgendaDao,
) : LocalAgendaDataSource {

    // Reminders
    override fun getAllReminders(): Flow<List<AgendaItem.Reminder>> {
        return agendaDao.getAllReminders()
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override fun getRemindersForDate(targetDate: String): Flow<List<AgendaItem.Reminder>> {
        return agendaDao.getRemindersForDate(targetDate)
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override suspend fun upsertReminder(
        reminder: AgendaItem.Reminder,
    ): Result<ReminderId, DataError.Local> {
        return try {
            val entity = reminder.toReminderEntity()
            agendaDao.upsertReminder(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertReminders(
        reminders: List<AgendaItem.Reminder>,
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

    // Tasks
    override fun getAllTasks(): Flow<List<AgendaItem.Task>> {
        return agendaDao.getAllTasks()
            .map { taskEntities ->
                taskEntities.map { it.toTask() }
            }
    }

    override fun getTasksForDate(targetDate: String): Flow<List<AgendaItem.Task>> {
        return agendaDao.getTasksForDate(targetDate)
            .map { taskEntities ->
                taskEntities.map { it.toTask() }
            }
    }

    override suspend fun upsertTask(task: AgendaItem.Task): Result<TaskId, DataError.Local> {
        return try {
            val entity = task.toTaskEntity()
            agendaDao.upsertTask(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertTasks(
        tasks: List<AgendaItem.Task>,
    ): Result<List<TaskId>, DataError.Local> {
        return try {
            val entities = tasks.map { it.toTaskEntity() }
            agendaDao.upsertTasks(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String) {
        agendaDao.deleteTask(id)
    }

    override suspend fun deleteAllTasks() {
        agendaDao.deleteAllTasks()
    }

    // All
    override fun getAllAgendaItems(): Flow<List<AgendaItem>> {
        return getAllReminders()
            .combine(getAllTasks()) { reminders, tasks ->
                (reminders + tasks).sortedBy { it.startDateTime }
            }
    }

    override fun getAgendaItemsForDate(targetDate: String): Flow<List<AgendaItem>> {
        return getRemindersForDate(targetDate)
            .combine(getTasksForDate(targetDate)) { reminders, tasks ->
                (reminders + tasks).sortedBy { it.startDateTime }
            }
    }

    override suspend fun deleteAllAgendaItems() {
        agendaDao.deleteAllReminders()
        agendaDao.deleteAllTasks()
    }
}
