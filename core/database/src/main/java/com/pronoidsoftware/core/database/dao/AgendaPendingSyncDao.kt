package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pronoidsoftware.core.database.entity.sync.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedTaskSyncEntity
import com.pronoidsoftware.core.database.entity.sync.ReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.TaskPendingSyncEntity
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId

@Dao
interface AgendaPendingSyncDao {

    // Created Reminders
    @Query("SELECT * FROM reminderpendingsyncentity WHERE userId=:userId")
    suspend fun getAllReminderPendingSyncEntities(userId: String): List<ReminderPendingSyncEntity>

    @Query("SELECT * FROM reminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun getReminderPendingSyncEntity(reminderId: ReminderId): ReminderPendingSyncEntity?

    @Upsert
    suspend fun upsertReminderPendingSyncEntity(
        reminderPendingSyncEntity: ReminderPendingSyncEntity,
    )

    @Query("DELETE FROM reminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun deleteReminderPendingSyncEntity(reminderId: ReminderId)

    // Deleted Reminders
    @Query("SELECT * FROM deletedremindersyncentity WHERE userId=:userId")
    suspend fun getAllDeletedReminderSyncEntities(userId: String): List<DeletedReminderSyncEntity>

    @Upsert
    suspend fun upsertDeletedReminderSyncEntity(entity: DeletedReminderSyncEntity)

    @Query("DELETE FROM deletedremindersyncentity WHERE reminderId=:reminderId")
    suspend fun deleteDeletedReminderSyncEntity(reminderId: ReminderId)

    // Created Tasks
    @Query("SELECT * FROM taskpendingsyncentity WHERE userId=:userId")
    suspend fun getAllTaskPendingSyncEntities(userId: String): List<TaskPendingSyncEntity>

    @Query("SELECT * FROM taskpendingsyncentity WHERE taskId=:taskId")
    suspend fun getTaskPendingSyncEntity(taskId: TaskId): TaskPendingSyncEntity?

    @Upsert
    suspend fun upsertTaskPendingSyncEntity(taskPendingSyncEntity: TaskPendingSyncEntity)

    @Query("DELETE FROM taskpendingsyncentity WHERE taskId=:taskId")
    suspend fun deleteTaskPendingSyncEntity(taskId: TaskId)

    // Deleted Tasks
    @Query("SELECT * FROM deletedtasksyncentity WHERE userId=:userId")
    suspend fun getAllDeletedTaskSyncEntities(userId: String): List<DeletedTaskSyncEntity>

    @Upsert
    suspend fun upsertDeletedTaskSyncEntity(entity: DeletedTaskSyncEntity)

    @Query("DELETE FROM deletedtasksyncentity WHERE taskId=:taskId")
    suspend fun deleteDeletedTaskSyncEntity(taskId: TaskId)
}
