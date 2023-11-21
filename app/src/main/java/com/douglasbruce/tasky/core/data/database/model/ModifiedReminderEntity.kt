package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.ModificationType

@Entity(
    tableName = "modified_reminders"
)
data class ModifiedReminderEntity(
    @PrimaryKey(autoGenerate = false) val reminderId: String,
    val type: ModificationType,
)