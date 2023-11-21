package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.ModificationType

@Entity(
    tableName = "modified_events"
)
data class ModifiedEventEntity(
    @PrimaryKey(autoGenerate = false) val eventId: String,
    val type: ModificationType,
)