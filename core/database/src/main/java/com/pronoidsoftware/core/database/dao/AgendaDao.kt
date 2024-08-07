package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
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
}
