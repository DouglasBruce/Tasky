package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.AgendaItemType
import com.douglasbruce.tasky.core.model.ModificationType

@Entity(
    tableName = "modified_agenda"
)
data class ModifiedAgendaItemEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val agendaItemType: AgendaItemType,
    val modificationType: ModificationType,
)