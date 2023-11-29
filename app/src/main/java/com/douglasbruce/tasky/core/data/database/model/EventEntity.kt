package com.douglasbruce.tasky.core.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.Attendee
import com.douglasbruce.tasky.core.model.NotificationType
import java.util.UUID

@Entity(
    tableName = "events"
)
data class EventEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val notificationType: NotificationType,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val photos: List<AgendaPhoto.Local>
)