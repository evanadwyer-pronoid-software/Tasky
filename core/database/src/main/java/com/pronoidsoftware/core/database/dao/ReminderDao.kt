package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pronoidsoftware.core.database.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

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
}
