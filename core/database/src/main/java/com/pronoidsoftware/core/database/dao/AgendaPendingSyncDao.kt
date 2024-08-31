package com.pronoidsoftware.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pronoidsoftware.core.database.entity.sync.CreatedEventPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.CreatedTaskPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedEventSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedReminderSyncEntity
import com.pronoidsoftware.core.database.entity.sync.DeletedTaskSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedEventPendingSyncAttendeeEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedEventPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedEventPendingSyncEntityWithAttendeeIds
import com.pronoidsoftware.core.database.entity.sync.UpdatedReminderPendingSyncEntity
import com.pronoidsoftware.core.database.entity.sync.UpdatedTaskPendingSyncEntity
import com.pronoidsoftware.core.domain.agendaitem.EventId
import com.pronoidsoftware.core.domain.agendaitem.ReminderId
import com.pronoidsoftware.core.domain.agendaitem.TaskId

@Dao
interface AgendaPendingSyncDao {

    // Created Reminders
    @Query("SELECT * FROM createdreminderpendingsyncentity WHERE userId=:userId")
    suspend fun getAllCreatedReminderPendingSyncEntities(
        userId: String,
    ): List<CreatedReminderPendingSyncEntity>

    @Query("SELECT * FROM createdreminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun getCreatedReminderPendingSyncEntity(
        reminderId: ReminderId,
    ): CreatedReminderPendingSyncEntity?

    @Upsert
    suspend fun upsertCreatedReminderPendingSyncEntity(
        createdReminderPendingSyncEntity: CreatedReminderPendingSyncEntity,
    )

    @Query("DELETE FROM createdreminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun deleteCreatedReminderPendingSyncEntity(reminderId: ReminderId)

    // Updated Reminders
    @Query("SELECT * FROM updatedreminderpendingsyncentity WHERE userId=:userId")
    suspend fun getAllUpdatedReminderPendingSyncEntities(
        userId: String,
    ): List<UpdatedReminderPendingSyncEntity>

    @Query("SELECT * FROM updatedreminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun getUpdatedReminderPendingSyncEntity(
        reminderId: ReminderId,
    ): UpdatedReminderPendingSyncEntity?

    @Upsert
    suspend fun upsertUpdatedReminderPendingSyncEntity(
        updatedReminderPendingSyncEntity: UpdatedReminderPendingSyncEntity,
    )

    @Query("DELETE FROM updatedreminderpendingsyncentity WHERE reminderId=:reminderId")
    suspend fun deleteUpdatedReminderPendingSyncEntity(reminderId: ReminderId)

    // Deleted Reminders
    @Query("SELECT * FROM deletedremindersyncentity WHERE userId=:userId")
    suspend fun getAllDeletedReminderSyncEntities(userId: String): List<DeletedReminderSyncEntity>

    @Upsert
    suspend fun upsertDeletedReminderSyncEntity(entity: DeletedReminderSyncEntity)

    @Query("DELETE FROM deletedremindersyncentity WHERE reminderId=:reminderId")
    suspend fun deleteDeletedReminderSyncEntity(reminderId: ReminderId)

    // Created Tasks
    @Query("SELECT * FROM createdtaskpendingsyncentity WHERE userId=:userId")
    suspend fun getAllCreatedTaskPendingSyncEntities(
        userId: String,
    ): List<CreatedTaskPendingSyncEntity>

    @Query("SELECT * FROM createdtaskpendingsyncentity WHERE taskId=:taskId")
    suspend fun getCreatedTaskPendingSyncEntity(taskId: TaskId): CreatedTaskPendingSyncEntity?

    @Upsert
    suspend fun upsertCreatedTaskPendingSyncEntity(
        createdTaskPendingSyncEntity: CreatedTaskPendingSyncEntity,
    )

    @Query("DELETE FROM createdtaskpendingsyncentity WHERE taskId=:taskId")
    suspend fun deleteCreatedTaskPendingSyncEntity(taskId: TaskId)

    // Updated Tasks
    @Query("SELECT * FROM updatedtaskpendingsyncentity WHERE userId=:userId")
    suspend fun getAllUpdatedTaskPendingSyncEntities(
        userId: String,
    ): List<UpdatedTaskPendingSyncEntity>

    @Query("SELECT * FROM updatedtaskpendingsyncentity WHERE taskId=:taskId")
    suspend fun getUpdatedTaskPendingSyncEntity(taskId: TaskId): UpdatedTaskPendingSyncEntity?

    @Upsert
    suspend fun upsertUpdatedTaskPendingSyncEntity(
        updatedTaskPendingSyncEntity: UpdatedTaskPendingSyncEntity,
    )

    @Query("DELETE FROM updatedtaskpendingsyncentity WHERE taskId=:taskId")
    suspend fun deleteUpdatedTaskPendingSyncEntity(taskId: TaskId)

    // Deleted Tasks
    @Query("SELECT * FROM deletedtasksyncentity WHERE userId=:userId")
    suspend fun getAllDeletedTaskSyncEntities(userId: String): List<DeletedTaskSyncEntity>

    @Upsert
    suspend fun upsertDeletedTaskSyncEntity(entity: DeletedTaskSyncEntity)

    @Query("DELETE FROM deletedtasksyncentity WHERE taskId=:taskId")
    suspend fun deleteDeletedTaskSyncEntity(taskId: TaskId)

    // Created Events
    @Query("SELECT * FROM createdeventpendingsyncentity WHERE userId=:userId")
    suspend fun getAllCreatedEventPendingSyncEntities(
        userId: String,
    ): List<CreatedEventPendingSyncEntity>

    @Query("SELECT * FROM createdeventpendingsyncentity WHERE eventId=:eventId")
    suspend fun getCreatedEventPendingSyncEntity(eventId: EventId): CreatedEventPendingSyncEntity?

    @Upsert
    suspend fun upsertCreatedEventPendingSyncEntity(
        createdEventPendingSyncEntity: CreatedEventPendingSyncEntity,
    )

    @Query("DELETE FROM createdeventpendingsyncentity WHERE eventId=:eventId")
    suspend fun deleteCreatedEventPendingSyncEntity(eventId: EventId)

    // Updated Events
    @Query("SELECT * FROM updatedeventpendingsyncentity WHERE userId=:userId")
    suspend fun getAllUpdatedEventPendingSyncEntitiesWithAttendeeIds(
        userId: String,
    ): List<UpdatedEventPendingSyncEntityWithAttendeeIds>

    @Query("SELECT * FROM updatedeventpendingsyncentity WHERE eventId=:eventId")
    suspend fun getUpdatedEventPendingSyncEntityWithAttendeeIds(
        eventId: EventId,
    ): UpdatedEventPendingSyncEntityWithAttendeeIds?

    @Upsert
    suspend fun upsertUpdatedEventPendingSyncEntity(
        updatedEventPendingSyncEntity: UpdatedEventPendingSyncEntity,
    )

    @Query("DELETE FROM updatedeventpendingsyncentity WHERE eventId=:eventId")
    suspend fun deleteUpdatedEventPendingSyncEntity(eventId: EventId)

    @Upsert
    suspend fun upsertUpdatedEventPendingSyncAttendeeEntity(
        updatedEventPendingSyncAttendeeEntity: UpdatedEventPendingSyncAttendeeEntity,
    )

    @Upsert
    suspend fun upsertUpdatedEventPendingSyncAttendeeEntities(
        updatedEventPendingSyncAttendeeEntities: List<UpdatedEventPendingSyncAttendeeEntity>,
    )

    @Query("DELETE FROM updatedeventpendingsyncattendeeentity WHERE eventId=:eventId")
    suspend fun deleteUpdatedEventPendingSyncAttendeeEntities(eventId: EventId)

    // Deleted Events
    @Query("SELECT * FROM deletedeventsyncentity WHERE userId=:userId")
    suspend fun getAllDeletedEventSyncEntities(userId: String): List<DeletedEventSyncEntity>

    @Upsert
    suspend fun upsertDeletedEventSyncEntity(entity: DeletedEventSyncEntity)

    @Query("DELETE FROM deletedeventsyncentity WHERE eventId=:eventId")
    suspend fun deleteDeletedEventSyncEntity(eventId: EventId)
}
