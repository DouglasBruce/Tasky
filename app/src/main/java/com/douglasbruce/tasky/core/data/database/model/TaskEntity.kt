package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.NotificationType
import java.util.UUID

@Entity(
    tableName = "tasks"
)
data class TaskEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean,
    val notificationType: NotificationType,
)