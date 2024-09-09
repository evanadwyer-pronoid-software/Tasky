package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.pronoidsoftware.core.database.entity.TaskEntity
import com.pronoidsoftware.core.domain.agendaitem.TaskId

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
data class UpdatedTaskPendingSyncEntity(
    @Embedded val task: TaskEntity,
    @PrimaryKey(autoGenerate = false)
    val taskId: TaskId = task.id,
    val userId: String,
)
