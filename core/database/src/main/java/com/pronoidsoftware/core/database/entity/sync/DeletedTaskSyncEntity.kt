package com.pronoidsoftware.core.database.entity.sync

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pronoidsoftware.core.domain.agendaitem.TaskId

@Entity
data class DeletedTaskSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val taskId: TaskId,
    val userId: String,
)
