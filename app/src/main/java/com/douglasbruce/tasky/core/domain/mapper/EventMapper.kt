package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.network.model.NetworkEvent
import com.douglasbruce.tasky.core.network.model.request.CreateEventRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateEventRequest
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun AgendaItem.Event.toEventEntity(): EventEntity {
    val utcFromTime = this.from.withZoneSameInstant(ZoneId.of("UTC"))
    val utcToTime = this.to.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return EventEntity(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = utcFromTime.toEpochSecond() * 1000,
        to = utcToTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        host = this.host ?: "",
        notificationType = this.eventNotificationType,
        isUserEventCreator = this.isUserEventCreator
    )
}

fun EventEntity.toEvent(): AgendaItem.Event {
    return AgendaItem.Event(
        eventId = this.id,
        eventTitle = this.title,
        eventDescription = this.description,
        from = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.from), ZoneId.systemDefault()),
        to = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.to), ZoneId.systemDefault()),
        remindAtTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(this.remindAt),
            ZoneId.systemDefault()
        ),
        host = this.host,
        eventNotificationType = this.notificationType,
        isUserEventCreator = this.isUserEventCreator,
        photos = emptyList(), //TODO: Update value to reflect real state
        attendees = emptyList(), //TODO: Update value to reflect real state
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
    val utcFromTime = this.from.withZoneSameInstant(ZoneId.of("UTC"))
    val utcToTime = this.to.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return CreateEventRequest(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = utcFromTime.toEpochSecond() * 1000,
        to = utcToTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        attendeeIds = this.attendees.map { it.userId }
    )
}

fun AgendaItem.Event.toUpdateEventRequest(
    deletedPhotoKeys: List<String>,
    isGoing: Boolean,
): UpdateEventRequest {
    val utcFromTime = this.from.withZoneSameInstant(ZoneId.of("UTC"))
    val utcToTime = this.to.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return UpdateEventRequest(
        id = this.eventId,
        title = this.eventTitle,
        description = this.eventDescription ?: "",
        from = utcFromTime.toEpochSecond() * 1000,
        to = utcToTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        attendeeIds = this.attendees.map { it.userId },
        deletedPhotoKeys = deletedPhotoKeys,
        isGoing = isGoing
    )
}