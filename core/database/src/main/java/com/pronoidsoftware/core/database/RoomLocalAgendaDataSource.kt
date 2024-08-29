package com.pronoidsoftware.core.database

import android.database.sqlite.SQLiteFullException
import com.pronoidsoftware.core.database.dao.AgendaDao
import com.pronoidsoftware.core.database.entity.AttendeeEntity
import com.pronoidsoftware.core.database.entity.PhotoEntity
import com.pronoidsoftware.core.database.mappers.toAttendeeEntity
import com.pronoidsoftware.core.database.mappers.toEvent
import com.pronoidsoftware.core.database.mappers.toEventEntity
import com.pronoidsoftware.core.database.mappers.toPhotoEntity
import com.pronoidsoftware.core.database.mappers.toReminder
import com.pronoidsoftware.core.database.mappers.toReminderEntity
import com.pronoidsoftware.core.database.mappers.toTask
import com.pronoidsoftware.core.database.mappers.toTaskEntity
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId
import com.pronoidsoftware.core.domain.util.DataError
import com.pronoidsoftware.core.domain.util.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

class RoomLocalAgendaDataSource @Inject constructor(
    private val agendaDao: AgendaDao,
) : LocalAgendaDataSource {

    // Reminders
    override suspend fun getReminder(id: ReminderId): AgendaItem.Reminder? {
        return agendaDao.getReminder(id)?.toReminder()
    }

    override fun getAllReminders(): Flow<List<AgendaItem.Reminder>> {
        return agendaDao.getAllReminders()
            .map { reminderEntities ->
                reminderEntities.map { it.toReminder() }
            }
    }

    override suspend fun getAllRemindersSnapshot(): List<AgendaItem.Reminder> {
        return agendaDao.getAllRemindersSnapshot().map { it.toReminder() }
    }

    override fun getRemindersForDate(targetDate: LocalDate): Flow<List<AgendaItem.Reminder>> {
        val beginningTime = targetDate
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        val endingTime = targetDate
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        return agendaDao.getRemindersForDate(beginningTime, endingTime)
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
    override suspend fun getTask(id: TaskId): AgendaItem.Task? {
        return agendaDao.getTask(id)?.toTask()
    }

    override fun getAllTasks(): Flow<List<AgendaItem.Task>> {
        return agendaDao.getAllTasks()
            .map { taskEntities ->
                taskEntities.map { it.toTask() }
            }
    }

    override suspend fun getAllTasksSnapshot(): List<AgendaItem.Task> {
        return agendaDao.getAllTasksSnapshot().map { it.toTask() }
    }

    override fun getTasksForDate(targetDate: LocalDate): Flow<List<AgendaItem.Task>> {
        val beginningTime = targetDate
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        val endingTime = targetDate
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        return agendaDao.getTasksForDate(beginningTime, endingTime)
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

    // Events
    override suspend fun getEvent(id: EventId): AgendaItem.Event? {
        return agendaDao.getEvent(id)?.toEvent()
    }

    override fun getAllEvents(): Flow<List<AgendaItem.Event>> {
        return agendaDao.getAllEvents()
            .map { eventEntities ->
                eventEntities.map { it.toEvent() }
            }
    }

    override suspend fun getAllEventsSnapshot(): List<AgendaItem.Event> {
        return agendaDao.getAllEventsSnapshot().map { it.toEvent() }
    }

    override fun getEventsForDate(targetDate: LocalDate): Flow<List<AgendaItem.Event>> {
        val beginningTime = targetDate
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        val endingTime = targetDate
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        return agendaDao.getEventsForDate(beginningTime, endingTime)
            .map { eventEntities ->
                eventEntities.map { it.toEvent() }
            }
    }

    override suspend fun upsertEvent(event: AgendaItem.Event): Result<EventId, DataError.Local> {
        return try {
            val eventEntity = event.toEventEntity()
            val photoEntitiesToAdd = event.photos
                .filterIsInstance<Photo.Remote>()
                .map {
                    it.toPhotoEntity(event.id)
                }
            val photoEntitiesToDelete = event.deletedPhotos
                .map { it.toPhotoEntity(event.id) }
            val attendeeEntitiesToAdd = event.attendees.map {
                it.toAttendeeEntity(event.id)
            }
            val attendeeEntitiesToDelete = event.deletedAttendees
                .map { it.toAttendeeEntity(event.id) }
            agendaDao.upsertEventWithPhotosAndAttendees(
                event = eventEntity,
                photosToAdd = photoEntitiesToAdd,
                photosToDelete = photoEntitiesToDelete,
                attendeesToAdd = attendeeEntitiesToAdd,
                attendeesToDelete = attendeeEntitiesToDelete,
            )
            Result.Success(eventEntity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertEvents(
        events: List<AgendaItem.Event>,
    ): Result<List<EventId>, DataError.Local> {
        return try {
            val eventEntities = events.map { it.toEventEntity() }
            val photoEntities = mutableListOf<PhotoEntity>()
            events.forEach { event ->
                photoEntities.addAll(
                    event.photos
                        .filterIsInstance<Photo.Remote>()
                        .map {
                            it.toPhotoEntity(event.id)
                        },
                )
            }
            val attendeeEntities = mutableListOf<AttendeeEntity>()
            events.forEach { event ->
                attendeeEntities.addAll(
                    event.attendees.map {
                        it.toAttendeeEntity(event.id)
                    },
                )
            }
            agendaDao.upsertEventsWithPhotosAndAttendees(
                events = eventEntities,
                photos = photoEntities,
                attendees = attendeeEntities,
            )
            Result.Success(eventEntities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteEvent(id: String) {
        agendaDao.deleteEventWithPhotosAndAttendees(id)
    }

    override suspend fun deleteAllEvents() {
        agendaDao.deleteAllEventsAndPhotosAndAttendees()
    }

    // All
    override suspend fun upsertAgendaItems(
        reminders: List<AgendaItem.Reminder>,
        tasks: List<AgendaItem.Task>,
        events: List<AgendaItem.Event>,
    ): Result<Map<AgendaItemType, List<String>>, DataError.Local> {
        return try {
            val reminderEntities = reminders.map { it.toReminderEntity() }
            val taskEntities = tasks.map { it.toTaskEntity() }
            val eventEntities = events.map { it.toEventEntity() }
            val photoEntities = events.flatMap { event ->
                event.photos
                    .filterIsInstance<Photo.Remote>()
                    .map { it.toPhotoEntity(event.id) }
            }
            val attendeeEntities = events.flatMap { event ->
                event.attendees.map { it.toAttendeeEntity(event.id) }
            }
            agendaDao.upsertAllAgendaItems(
                reminders = reminderEntities,
                tasks = taskEntities,
                events = eventEntities,
                photos = photoEntities,
                attendees = attendeeEntities,
            )
            Result.Success(
                mapOf(
                    AgendaItemType.REMINDER to reminders.map { it.id },
                    AgendaItemType.TASK to tasks.map { it.id },
                    AgendaItemType.EVENT to events.map { it.id },
                ),
            )
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAllAgendaItems(): Flow<List<AgendaItem>> {
        return combine(
            getAllReminders(),
            getAllTasks(),
            getAllEvents(),
        ) { reminders, tasks, events ->
            (reminders + tasks + events).sortedBy { it.startDateTime }
        }
    }

    override suspend fun getAllAgendaItemsSnapshot(): List<AgendaItem> {
        val reminders = getAllRemindersSnapshot()
        val tasks = getAllTasksSnapshot()
        val events = getAllEventsSnapshot()
        return reminders + tasks + events
    }

    override fun getAgendaItemsForDate(targetDate: LocalDate): Flow<List<AgendaItem>> {
        return combine(
            getRemindersForDate(targetDate),
            getTasksForDate(targetDate),
            getEventsForDate(targetDate),
        ) { reminders, tasks, events ->
            (reminders + tasks + events).sortedBy { it.startDateTime }
        }
    }

    override suspend fun deleteAllAgendaItems() {
        agendaDao.deleteAllAgendaItems()
    }
}
