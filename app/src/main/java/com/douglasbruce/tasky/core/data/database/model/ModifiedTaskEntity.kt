package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.ModificationType

@Entity(
    tableName = "modified_tasks"
)
data class ModifiedTaskEntity(
    @PrimaryKey(autoGenerate = false) val taskId: String,
    val type: ModificationType,
)