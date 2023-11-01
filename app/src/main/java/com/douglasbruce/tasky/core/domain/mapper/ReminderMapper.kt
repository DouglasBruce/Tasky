package com.douglasbruce.tasky.core.domain.mapper

import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.core.network.model.NetworkReminder
import com.douglasbruce.tasky.core.network.model.request.CreateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateReminderRequest
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun AgendaItem.Reminder.toReminderEntity(): ReminderEntity {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return ReminderEntity(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
        notificationType = this.reminderNotificationType
    )
}

fun ReminderEntity.toReminder(): AgendaItem.Reminder {
    return AgendaItem.Reminder(
        reminderId = this.id,
        reminderTitle = this.title,
        reminderDescription = this.description,
        time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault()),
        remindAtTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault()),
        reminderNotificationType = this.notificationType
    )
}

fun NetworkReminder.toReminder(): AgendaItem.Reminder {
    val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    val remindAt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.remindAt), ZoneId.systemDefault())
    return AgendaItem.Reminder(
        reminderId = this.id,
        reminderTitle = this.title,
        reminderDescription = this.description,
        time = time,
        remindAtTime = remindAt,
        reminderNotificationType = NotificationType.dateTimeToNotificationType(time, remindAt)
    )
}

fun AgendaItem.Reminder.toCreateReminderRequest(): CreateReminderRequest {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return CreateReminderRequest(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
    )
}

fun AgendaItem.Reminder.toUpdateReminderRequest(): UpdateReminderRequest {
    val utcTime = this.time.withZoneSameInstant(ZoneId.of("UTC"))
    val utcRemindAtTime = this.remindAt.withZoneSameInstant(ZoneId.of("UTC"))

    return UpdateReminderRequest(
        id = this.reminderId,
        title = this.reminderTitle,
        description = this.reminderDescription ?: "",
        time = utcTime.toEpochSecond() * 1000,
        remindAt = utcRemindAtTime.toEpochSecond() * 1000,
    )
}