package com.douglasbruce.tasky.core.domain.mapper

import androidx.core.net.toUri
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.AlarmItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.network.model.NetworkEvent
import com.douglasbruce.tasky.core.network.model.request.CreateEventRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateEventRequest
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun AgendaItem.Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = this.from.toInstant().toEpochMilli(),
        to = this.to.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
        host = this.host ?: "",
        notificationType = this.eventNotificationType,
        isUserEventCreator = this.isUserEventCreator,
        attendees = this.attendees,
        photos = this.photos.map { AgendaPhoto.Local(it.key(), it.uri().toUri()) },
    )
}

fun EventEntity.toEvent(): AgendaItem.Event {
    val from = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.from), ZoneId.systemDefault())
    val to = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.to), ZoneId.systemDefault())
    val remindAt =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())

    return AgendaItem.Event(
        eventId = this.id,
        eventTitle = this.title,
        eventDescription = this.description,
        from = from,
        to = to,
        remindAtTime = remindAt,
        host = this.host,
        eventNotificationType = this.notificationType,
        isUserEventCreator = this.isUserEventCreator,
        photos = this.photos,
        attendees = this.attendees,
    )
}

fun NetworkEvent.toEvent(): AgendaItem.Event {
    val from = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.from), ZoneId.systemDefault())
    val to = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.to), ZoneId.systemDefault())
    val remindAt =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())

    return AgendaItem.Event(
        eventId = this.id,
        eventTitle = this.title,
        eventDescription = this.description,
        from = from,
        to = to,
        remindAtTime = remindAt,
        host = this.host,
        isUserEventCreator = this.isUserEventCreator,
        photos = this.photos.map { AgendaPhoto.Remote(it.key, it.url) },
        attendees = this.attendees.toAttendees(),
        eventNotificationType = NotificationType.dateTimeToNotificationType(from, remindAt)
    )
}

fun AgendaItem.Event.toCreateEventRequest(): CreateEventRequest {
    return CreateEventRequest(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = this.from.toInstant().toEpochMilli(),
        to = this.to.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
        attendeeIds = this.attendees.map { it.userId }
    )
}

fun AgendaItem.Event.toUpdateEventRequest(
    deletedPhotoKeys: List<String>,
    isGoing: Boolean,
): UpdateEventRequest {
    return UpdateEventRequest(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = this.from.toInstant().toEpochMilli(),
        to = this.to.toInstant().toEpochMilli(),
        remindAt = this.remindAtTime.toInstant().toEpochMilli(),
        attendeeIds = this.attendees.map { it.userId },
        deletedPhotoKeys = deletedPhotoKeys,
        isGoing = isGoing
    )
}

fun AgendaItem.Event.toAlarmItem(): AlarmItem {
    return AlarmItem(
        id = this.eventId,
        title = this.eventTitle,
        text = this.eventDescription ?: "",
        time = this.remindAtTime.toLocalDateTime(),
        date = this.from.toInstant().toEpochMilli(),
        type = "Event",
    )
}