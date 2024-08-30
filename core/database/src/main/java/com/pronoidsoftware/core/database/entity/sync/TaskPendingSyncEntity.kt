package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.database.entity.TaskEntity
import com.pronoidsoftware.core.domain.agendaitem.TaskId

@Entity
data class TaskPendingSyncEntity(
    @Embedded val task: TaskEntity,
    @PrimaryKey(autoGenerate = false)
    val taskId: TaskId = task.id,
    val userId: String,
)
