package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pronoidsoftware.core.database.entity.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.ReminderPendingSyncEntity
import com.pronoidsoftware.core.domain.agendaitem.ReminderId

@Dao
interface AgendaPendingSyncDao {

    // Created Reminders
    @Query("SELECT * FROM reminderpendingsyncentity WHERE userId=:userId")
    suspend fun getAllReminderPendingSyncEntities(userId: String): List<ReminderPendingSyncEntity>

    @Query("SELECT * FROM reminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun getReminderPendingSyncEntity(reminderId: ReminderId): ReminderPendingSyncEntity?

    @Upsert
    suspend fun upsertReminderPendingSyncEntity(reminderEntity: ReminderPendingSyncEntity)

    @Query("DELETE FROM reminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun deleteReminderPendingSyncEntity(reminderId: ReminderId)

    // Deleted Reminders
    @Query("SELECT * FROM deletedremindersyncentity WHERE userId=:userId")
    suspend fun getAllDeletedReminderSyncEntities(userId: String): List<DeletedReminderSyncEntity>

    @Upsert
    suspend fun upsertDeletedReminderSyncEntity(entity: DeletedReminderSyncEntity)

    @Query("DELETE FROM deletedremindersyncentity WHERE reminderId=:reminderId")
    suspend fun deleteDeletedReminderSyncEntity(reminderId: ReminderId)
}
