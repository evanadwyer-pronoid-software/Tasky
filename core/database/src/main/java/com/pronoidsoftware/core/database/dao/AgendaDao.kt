package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pronoidsoftware.core.database.entity.AttendeeEntity
import com.pronoidsoftware.core.database.entity.EventEntity
import com.pronoidsoftware.core.database.entity.EventWithAttendeesAndPhotos
import com.pronoidsoftware.core.database.entity.PhotoEntity
import com.pronoidsoftware.core.database.entity.ReminderEntity
import com.pronoidsoftware.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDao {

    // Reminders
    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminder: List<ReminderEntity>)

    @Query("SELECT * FROM reminderentity ORDER BY startDateTime")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query(
        "SELECT * FROM reminderentity " +
            "WHERE strftime('%m-%d-%Y', startDateTime/1000, 'unixepoch') = :targetDate " +
            "ORDER BY startDateTime",
    )
    fun getRemindersForDate(targetDate: String): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminderentity WHERE id=:id")
    suspend fun deleteReminder(id: String)

    @Query("DELETE FROM reminderentity")
    suspend fun deleteAllReminders()

    // Tasks
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Upsert
    suspend fun upsertTasks(task: List<TaskEntity>)

    @Query("SELECT * FROM taskentity ORDER BY startDateTime")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query(
        "SELECT * FROM taskentity " +
            "WHERE strftime('%m-%d-%Y', startDateTime/1000, 'unixepoch') = :targetDate " +
            "ORDER BY startDateTime",
    )
    fun getTasksForDate(targetDate: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM taskentity WHERE id=:id")
    suspend fun deleteTask(id: String)

    @Query("DELETE FROM taskentity")
    suspend fun deleteAllTasks()

    // Events
    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Upsert
    suspend fun upsertEvents(events: List<EventEntity>)

    @Upsert
    suspend fun upsertAttendee(attendee: AttendeeEntity)

    @Upsert
    suspend fun upsertAttendees(attendees: List<AttendeeEntity>)

    @Upsert
    suspend fun upsertPhoto(photo: PhotoEntity)

    @Upsert
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Transaction
    suspend fun upsertEventWithPhotosAndAttendees(
        event: EventEntity,
        photos: List<PhotoEntity>,
        attendees: List<AttendeeEntity>,
    ) {
        upsertEvent(event)
        upsertPhotos(photos)
        upsertAttendees(attendees)
    }

    @Transaction
    suspend fun upsertEventsWithPhotosAndAttendees(
        events: List<EventEntity>,
        photos: List<PhotoEntity>,
        attendees: List<AttendeeEntity>,
    ) {
        upsertEvents(events)
        upsertPhotos(photos)
        upsertAttendees(attendees)
    }

    @Transaction
    @Query("SELECT * FROM evententity ORDER BY startDateTime")
    fun getAllEvents(): Flow<List<EventWithAttendeesAndPhotos>>

    @Transaction
    @Query(
        "SELECT * FROM evententity " +
            "WHERE strftime('%m-%d-%Y', startDateTime/1000, 'unixepoch') = :targetDate " +
            "ORDER BY startDateTime",
    )
    fun getEventsForDate(targetDate: String): Flow<List<EventWithAttendeesAndPhotos>>

    @Query("DELETE FROM evententity WHERE id=:id")
    suspend fun deleteEvent(id: String)

    @Query("DELETE FROM evententity")
    suspend fun deleteAllEvents()

    @Query("DELETE FROM attendeeentity WHERE userId=:userId AND eventId=:eventId")
    suspend fun deleteAttendeeFromEvent(userId: String, eventId: String)

    @Query("DELETE FROM attendeeentity WHERE eventId=:id")
    suspend fun deleteAttendeesFromEvent(id: String)

    @Query("DELETE FROM attendeeentity")
    suspend fun deleteAllAttendees()

    @Query("DELETE FROM photoentity WHERE `key`=:id")
    suspend fun deletePhoto(id: String)

    @Query("DELETE FROM photoentity WHERE eventId=:id")
    suspend fun deletePhotosFromEvent(id: String)

    @Query("DELETE FROM photoentity")
    suspend fun deleteAllPhotos()

    @Transaction
    suspend fun deleteEventWithPhotosAndAttendees(id: String) {
        deleteEvent(id)
        deletePhotosFromEvent(id)
        deleteAttendeesFromEvent(id)
    }

    @Transaction
    suspend fun deleteAllEventsAndPhotosAndAttendees() {
        deleteAllEvents()
        deleteAllPhotos()
        deleteAllAttendees()
    }

    // All
    @Transaction
    suspend fun deleteAllAgendaItems() {
        deleteAllReminders()
        deleteAllTasks()
        deleteAllEventsAndPhotosAndAttendees()
    }
}
